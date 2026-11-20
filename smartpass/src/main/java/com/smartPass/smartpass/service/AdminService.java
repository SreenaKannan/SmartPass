package com.smartPass.smartpass.service;

import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.entity.Payment;
import com.smartPass.smartpass.repository.UserRepository;
import com.smartPass.smartpass.repository.CommuterCardRepository;
import com.smartPass.smartpass.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CommuterCardRepository cardRepository;
    private final PaymentRepository paymentRepository;

    public AdminService(
            UserRepository userRepository,
            CommuterCardRepository cardRepository,
            PaymentRepository paymentRepository) {

        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<CommuterCard> getAllCards() {
        return cardRepository.findAll().stream()
            .filter(card -> paymentRepository.existsByCardIdAndStatus(card.getCardId(), "SUCCESS"))
            .toList();
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll().stream()
            .filter(payment -> "SUCCESS".equals(payment.getStatus()))
            .toList();
    }
    public String approveCard(Long id) { return setStatus(id, "Approved"); }
    public String rejectCard(Long id) { return setStatus(id, "Rejected"); }
    public String suspendCard(Long id) { return setStatus(id, "Suspended"); }
    public String blockCard(Long id) { return setStatus(id, "Blocked"); }
    private String setStatus(Long id, String status) { CommuterCard card=cardRepository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Card not found")); card.setStatus(status); cardRepository.save(card); return "Card " + status.toLowerCase() + " successfully"; }
}
