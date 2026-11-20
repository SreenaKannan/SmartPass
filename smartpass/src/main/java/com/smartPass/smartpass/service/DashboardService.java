package com.smartPass.smartpass.service;

import com.smartPass.smartpass.dto.DashboardDTO;
import com.smartPass.smartpass.repository.CommuterCardRepository;
import com.smartPass.smartpass.repository.PaymentRepository;
import com.smartPass.smartpass.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.smartPass.smartpass.entity.User;

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

    public DashboardDTO getDashboardStats(User user) {

        DashboardDTO dashboard = new DashboardDTO();

        var cards = cardRepository.findByOwnerId(user.getId());
        dashboard.setTotalUsers(1);
        dashboard.setTotalCards(cards.size());
        dashboard.setActiveCards(cards.stream().filter(c -> "Approved".equals(c.getStatus())).count());
        dashboard.setRenewedCards(cards.stream().filter(c -> "Renewed".equals(c.getStatus())).count());
        dashboard.setBlockedCards(cards.stream().filter(c -> "Blocked".equals(c.getStatus())).count());
        dashboard.setTotalPayments(cards.stream().mapToLong(c -> paymentRepository.findByCardId(c.getCardId()).stream().filter(p -> "SUCCESS".equals(p.getStatus())).count()).sum());

        return dashboard;
    }
}
