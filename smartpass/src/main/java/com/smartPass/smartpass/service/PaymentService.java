package com.smartPass.smartpass.service;

import com.smartPass.smartpass.dto.PaymentConfirmRequest;
import com.smartPass.smartpass.dto.PaymentInitRequest;
import com.smartPass.smartpass.dto.PaymentOrderResponse;
import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.entity.Payment;
import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.repository.PaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sandbox payment gateway.
 *
 * Works like a real checkout: the SERVER decides the amount, a payment is an "order"
 * that moves INITIATED -> SUCCESS / FAILED / CANCELLED / EXPIRED, the session expires,
 * card numbers are Luhn-checked, card/net-banking payments need an OTP, and only a
 * successful payment can renew a pass. No real money moves and full card numbers / CVV
 * are never stored - only a masked label such as "Visa **** 1111".
 */
@Service
public class PaymentService {

    private static final long SESSION_MINUTES = 10;
    private static final int MAX_OTP_ATTEMPTS = 3;
    private static final String SANDBOX_OTP = "123456";

    private static final Pattern UPI = Pattern.compile("^[a-zA-Z0-9._-]{2,256}@[a-zA-Z][a-zA-Z0-9.-]{1,63}$");
    private static final Pattern EXPIRY = Pattern.compile("^(0[1-9]|1[0-2])/(\\d{2})$");
    private static final Pattern CVV = Pattern.compile("^\\d{3,4}$");
    private static final Set<String> BANKS = Set.of(
            "State Bank of India", "HDFC Bank", "ICICI Bank", "Axis Bank",
            "Kotak Mahindra Bank", "Punjab National Bank", "Bank of Baroda", "Canara Bank");

    private final PaymentRepository paymentRepository;
    private final CommuterCardService cardService;

    public PaymentService(PaymentRepository paymentRepository, CommuterCardService cardService) {
        this.paymentRepository = paymentRepository;
        this.cardService = cardService;
    }

    // ------------------------------------------------------------------
    // 1. Start checkout
    // ------------------------------------------------------------------
    public PaymentOrderResponse initiate(PaymentInitRequest req, User user) {
        if (req == null || req.getCardId() == null) {
            throw bad("Choose a pass to pay for.");
        }
        CommuterCard card = cardService.getOwnedCard(req.getCardId(), user);
        String purpose = req.getPurpose() == null ? "" : req.getPurpose().trim().toLowerCase();

        if (purpose.equals("application")) {
            if (!"Pending".equalsIgnoreCase(card.getStatus())) {
                throw bad("The application fee only applies to pending applications.");
            }
            if (isApplicationPaid(card)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "The application fee for this pass is already paid.");
            }
        } else if (purpose.equals("renewal")) {
            if (!"Approved".equalsIgnoreCase(card.getStatus())) {
                throw bad("Only approved passes can be renewed.");
            }
        } else {
            throw bad("Unknown payment purpose.");
        }
        if (card.getFareAmount() <= 0) {
            throw bad("No fare is configured for this pass.");
        }

        // Re-opening checkout (refresh / back button) resumes the same order instead of creating duplicates.
        Optional<Payment> open = paymentRepository
                .findFirstByCardIdAndPurposeAndStatusOrderByPaymentIdDesc(card.getCardId(), purpose, "INITIATED");
        if (open.isPresent()) {
            Payment existing = open.get();
            if (isExpired(existing)) {
                existing.setStatus("EXPIRED");
                existing.setFailureReason("Payment session expired.");
                paymentRepository.save(existing);
            } else {
                return toResponse(existing);
            }
        }

        Payment p = new Payment();
        p.setCardId(card.getCardId());
        p.setUserId(user.getId());
        p.setAmount(card.getFareAmount()); // always the server's figure
        p.setPurpose(purpose);
        p.setStatus("INITIATED");
        p.setOrderId("ORD" + randomToken());
        p.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(p);
        return toResponse(p);
    }

    // ------------------------------------------------------------------
    // 2. Pay
    // noRollbackFor: a wrong-OTP attempt must still be counted even though we answer with an error.
    // ------------------------------------------------------------------
    @Transactional(noRollbackFor = ResponseStatusException.class)
    public PaymentOrderResponse pay(String orderId, PaymentConfirmRequest req, User user) {
        Payment p = findOwnedOrder(orderId, user);

        if (!"INITIATED".equals(p.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This payment is already " + p.getStatus().toLowerCase() + ".");
        }
        if (isExpired(p)) {
            p.setStatus("EXPIRED");
            p.setFailureReason("Payment session expired. Please start again.");
            paymentRepository.save(p);
            return toResponse(p);
        }

        String method = req.getMethod() == null ? "" : req.getMethod().trim().toUpperCase();
        String label;
        String detail;
        String declineReason = null;
        boolean needsOtp;

        if (method.equals("UPI")) {
            String upi = validateUpi(req.getUpiId());
            label = "UPI";
            detail = maskUpi(upi);
            needsOtp = false;
            if (upi.toLowerCase().contains("fail")) {
                declineReason = "The UPI request was declined. Please try again or use another method.";
            }
        } else if (method.equals("CARD")) {
            String number = validateCard(req);
            label = "Card";
            detail = brandOf(number) + " \u2022\u2022\u2022\u2022 " + number.substring(number.length() - 4);
            needsOtp = true;
            if (number.equals("4000000000000002")) {
                declineReason = "Your card was declined by the issuing bank.";
            } else if (number.equals("4000000000009995")) {
                declineReason = "Insufficient funds. Please use a different payment method.";
            }
        } else if (method.equals("NETBANKING")) {
            String bank = req.getBank() == null ? "" : req.getBank().trim();
            if (!BANKS.contains(bank)) {
                throw bad("Select your bank.");
            }
            label = "Net Banking";
            detail = bank;
            needsOtp = true;
        } else {
            throw bad("Choose a payment method.");
        }

        if (needsOtp) {
            String otp = req.getOtp() == null ? "" : req.getOtp().trim();
            if (otp.isEmpty()) {
                throw bad("Enter the OTP to authorise this payment.");
            }
            if (!SANDBOX_OTP.equals(otp)) {
                int attempts = (p.getAttempts() == null ? 0 : p.getAttempts()) + 1;
                p.setAttempts(attempts);
                if (attempts >= MAX_OTP_ATTEMPTS) {
                    markFailed(p, label, detail, "Too many incorrect OTP attempts. The payment was stopped for your security.");
                    return toResponse(p);
                }
                paymentRepository.save(p);
                throw bad("Incorrect OTP. " + (MAX_OTP_ATTEMPTS - attempts) + " attempt(s) left.");
            }
        }

        if (declineReason != null) {
            markFailed(p, label, detail, declineReason);
            return toResponse(p);
        }

        p.setPaymentMethod(label);
        p.setMethodDetail(detail);
        p.setStatus("SUCCESS");
        p.setFailureReason(null);
        p.setTransactionId("TXN" + randomToken());
        p.setPaidAt(LocalDateTime.now());
        p.setPaymentDate(LocalDate.now().toString());
        paymentRepository.save(p);

        // The renewal only ever happens as a consequence of a successful payment.
        if ("renewal".equals(p.getPurpose())) {
            cardService.renewCard(p.getCardId());
        }
        return toResponse(p);
    }

    // ------------------------------------------------------------------
    // 3. Read / cancel / history
    // ------------------------------------------------------------------
    public PaymentOrderResponse getOrder(String orderId, User user) {
        Payment p = findOwnedOrder(orderId, user);
        if ("INITIATED".equals(p.getStatus()) && isExpired(p)) {
            p.setStatus("EXPIRED");
            p.setFailureReason("Payment session expired.");
            paymentRepository.save(p);
        }
        return toResponse(p);
    }

    public PaymentOrderResponse cancel(String orderId, User user) {
        Payment p = findOwnedOrder(orderId, user);
        if ("INITIATED".equals(p.getStatus())) {
            p.setStatus("CANCELLED");
            p.setFailureReason("Cancelled by you.");
            paymentRepository.save(p);
        }
        return toResponse(p);
    }

    public List<PaymentOrderResponse> myPayments(User user) {
        return paymentRepository.findByUserIdOrderByPaymentIdDesc(user.getId())
                .stream().map(this::toResponse).toList();
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByCardId(Long cardId) {
        return paymentRepository.findByCardId(cardId);
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------
    private void markFailed(Payment p, String label, String detail, String reason) {
        p.setPaymentMethod(label);
        p.setMethodDetail(detail);
        p.setStatus("FAILED");
        p.setFailureReason(reason);
        p.setPaymentDate(LocalDate.now().toString());
        paymentRepository.save(p);
    }

    private boolean isApplicationPaid(CommuterCard card) {
        // purpose == null covers payments made before checkout orders existed
        return paymentRepository.findByCardId(card.getCardId()).stream()
                .anyMatch(x -> "SUCCESS".equals(x.getStatus())
                        && (x.getPurpose() == null || "application".equals(x.getPurpose())));
    }

    private Payment findOwnedOrder(String orderId, User user) {
        return paymentRepository.findByOrderId(orderId)
                .filter(p -> user.getId().equals(p.getUserId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found."));
    }

    private boolean isExpired(Payment p) {
        return p.getCreatedAt() != null
                && p.getCreatedAt().plusMinutes(SESSION_MINUTES).isBefore(LocalDateTime.now());
    }

    private PaymentOrderResponse toResponse(Payment p) {
        PaymentOrderResponse r = new PaymentOrderResponse();
        r.setPaymentId(p.getPaymentId());
        r.setOrderId(p.getOrderId());
        r.setTransactionId(p.getTransactionId());
        r.setStatus(p.getStatus());
        r.setPurpose(p.getPurpose());
        r.setCardId(p.getCardId());
        r.setAmount(p.getAmount());
        r.setPaymentMethod(p.getPaymentMethod());
        r.setMethodDetail(p.getMethodDetail());
        r.setPaymentDate(p.getPaymentDate());
        r.setPaidAt(p.getPaidAt());
        r.setCreatedAt(p.getCreatedAt());
        r.setFailureReason(p.getFailureReason());

        if ("INITIATED".equals(p.getStatus()) && p.getCreatedAt() != null) {
            long left = Duration.between(LocalDateTime.now(), p.getCreatedAt().plusMinutes(SESSION_MINUTES)).getSeconds();
            r.setSecondsRemaining(Math.max(0, left));
        }

        CommuterCard card = p.getCardId() == null ? null : cardService.getCardById(p.getCardId());
        if (card != null) {
            r.setPassType(card.getPassType());
            r.setTransportType(card.getTransportType());
            r.setRoute(card.getSourceStation() + " \u2192 " + card.getDestinationStation());
            r.setCardExpiryDate(card.getExpiryDate() == null ? null : card.getExpiryDate().toString());
        }
        return r;
    }

    private String validateUpi(String upi) {
        String value = upi == null ? "" : upi.trim();
        if (!UPI.matcher(value).matches()) {
            throw bad("Enter a valid UPI ID, for example name@okaxis.");
        }
        return value;
    }

    /** Validates and returns the digits-only card number. */
    private String validateCard(PaymentConfirmRequest req) {
        String number = req.getCardNumber() == null ? "" : req.getCardNumber().replaceAll("[\\s-]", "");
        if (!number.matches("\\d{13,19}") || !luhn(number)) {
            throw bad("Enter a valid card number.");
        }
        String holder = req.getCardHolder() == null ? "" : req.getCardHolder().trim();
        if (holder.length() < 2 || !holder.matches("[A-Za-z .'-]+")) {
            throw bad("Enter the name printed on the card.");
        }
        String expiry = req.getExpiry() == null ? "" : req.getExpiry().trim();
        Matcher m = EXPIRY.matcher(expiry);
        if (!m.matches()) {
            throw bad("Enter the card expiry as MM/YY.");
        }
        YearMonth exp = YearMonth.of(2000 + Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)));
        if (exp.isBefore(YearMonth.now())) {
            throw bad("This card has expired.");
        }
        if (req.getCvv() == null || !CVV.matcher(req.getCvv().trim()).matches()) {
            throw bad("Enter a valid CVV.");
        }
        return number;
    }

    private boolean luhn(String digits) {
        int sum = 0;
        boolean doubleIt = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int d = digits.charAt(i) - '0';
            if (doubleIt) {
                d *= 2;
                if (d > 9) d -= 9;
            }
            sum += d;
            doubleIt = !doubleIt;
        }
        return sum % 10 == 0;
    }

    private String brandOf(String n) {
        if (n.startsWith("4")) return "Visa";
        int p2 = Integer.parseInt(n.substring(0, 2));
        int p4 = Integer.parseInt(n.substring(0, 4));
        if ((p2 >= 51 && p2 <= 55) || (p4 >= 2221 && p4 <= 2720)) return "Mastercard";
        if (p2 == 34 || p2 == 37) return "Amex";
        if (n.startsWith("508") || p2 == 60 || p2 == 65 || p2 == 81 || p2 == 82) return "RuPay";
        return "Card";
    }

    private String maskUpi(String upi) {
        int at = upi.indexOf('@');
        String name = upi.substring(0, at);
        String handle = upi.substring(at);
        return name.substring(0, Math.min(2, name.length())) + "***" + handle;
    }

    private String randomToken() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private ResponseStatusException bad(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
