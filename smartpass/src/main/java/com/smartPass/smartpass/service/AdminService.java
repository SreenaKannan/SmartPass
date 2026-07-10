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
        return cardRepository.findAll();
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}