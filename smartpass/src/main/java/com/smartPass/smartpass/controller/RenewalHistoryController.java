package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.entity.RenewalHistory;
import com.smartPass.smartpass.repository.RenewalHistoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/renewals")
public class RenewalHistoryController {

    private final RenewalHistoryRepository renewalRepository;

    public RenewalHistoryController(
            RenewalHistoryRepository renewalRepository) {

        this.renewalRepository = renewalRepository;
    }

    @GetMapping("/{cardId}")
    public List<RenewalHistory> getHistory(
            @PathVariable Long cardId) {

        return renewalRepository.findByCardId(cardId);
    }
}