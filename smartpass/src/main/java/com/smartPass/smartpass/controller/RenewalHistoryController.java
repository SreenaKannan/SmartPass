package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.entity.RenewalHistory;
import com.smartPass.smartpass.repository.RenewalHistoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.smartPass.smartpass.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.smartPass.smartpass.service.CommuterCardService;

@RestController
@RequestMapping("/api/renewals")
public class RenewalHistoryController {

    private final RenewalHistoryRepository renewalRepository;
    private final CommuterCardService cardService;

    public RenewalHistoryController(
            RenewalHistoryRepository renewalRepository, CommuterCardService cardService) {

        this.renewalRepository = renewalRepository;
        this.cardService = cardService;
    }

    @GetMapping("/{cardId}")
    public List<RenewalHistory> getHistory(
            @PathVariable Long cardId, @AuthenticationPrincipal User user) {
        cardService.getOwnedCard(cardId,user);

        return renewalRepository.findByCardId(cardId);
    }
}
