package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.dto.PaymentConfirmRequest;
import com.smartPass.smartpass.dto.PaymentInitRequest;
import com.smartPass.smartpass.dto.PaymentOrderResponse;
import com.smartPass.smartpass.entity.Payment;
import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.service.CommuterCardService;
import com.smartPass.smartpass.service.PaymentService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final CommuterCardService cardService;

    public PaymentController(PaymentService paymentService, CommuterCardService cardService) {
        this.paymentService = paymentService;
        this.cardService = cardService;
    }

    /** Step 1: open a checkout session. The amount is set by the server, never the client. */
    @PostMapping("/initiate")
    public PaymentOrderResponse initiate(@RequestBody PaymentInitRequest request,
                                         @AuthenticationPrincipal User user) {
        return paymentService.initiate(request, user);
    }

    /** Step 2: pay for an open checkout session. */
    @PostMapping("/{orderId}/pay")
    public PaymentOrderResponse pay(@PathVariable String orderId,
                                    @RequestBody PaymentConfirmRequest request,
                                    @AuthenticationPrincipal User user) {
        return paymentService.pay(orderId, request, user);
    }

    @PostMapping("/{orderId}/cancel")
    public PaymentOrderResponse cancel(@PathVariable String orderId,
                                       @AuthenticationPrincipal User user) {
        return paymentService.cancel(orderId, user);
    }

    @GetMapping("/{orderId}")
    public PaymentOrderResponse getOrder(@PathVariable String orderId,
                                         @AuthenticationPrincipal User user) {
        return paymentService.getOrder(orderId, user);
    }

    /** The signed-in user's payment history (newest first). */
    @GetMapping
    public List<PaymentOrderResponse> myPayments(@AuthenticationPrincipal User user) {
        return paymentService.myPayments(user);
    }

    @GetMapping("/card/{cardId}")
    public List<Payment> getPaymentsByCardId(@PathVariable Long cardId,
                                             @AuthenticationPrincipal User user) {
        cardService.getOwnedCard(cardId, user);
        return paymentService.getPaymentsByCardId(cardId);
    }
}
