package com.smartPass.smartpass.service;

import com.smartPass.smartpass.dto.DashboardDTO;
import com.smartPass.smartpass.repository.CommuterCardRepository;
import com.smartPass.smartpass.repository.PaymentRepository;
import com.smartPass.smartpass.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final CommuterCardRepository cardRepository;
    private final PaymentRepository paymentRepository;

    public DashboardService(
            UserRepository userRepository,
            CommuterCardRepository cardRepository,
            PaymentRepository paymentRepository) {

        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.paymentRepository = paymentRepository;
    }

    public DashboardDTO getDashboardStats() {

        DashboardDTO dashboard = new DashboardDTO();

        dashboard.setTotalUsers(userRepository.count());

        dashboard.setTotalCards(cardRepository.count());

        dashboard.setActiveCards(
                cardRepository.countByStatus("Active"));

        dashboard.setRenewedCards(
                cardRepository.countByStatus("Renewed"));

        dashboard.setBlockedCards(
                cardRepository.countByStatus("Blocked"));

        dashboard.setTotalPayments(
                paymentRepository.count());

        return dashboard;
    }
}