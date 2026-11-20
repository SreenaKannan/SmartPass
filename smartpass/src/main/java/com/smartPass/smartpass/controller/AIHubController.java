package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.dto.*;
import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.service.AIHubService;
import com.smartPass.smartpass.service.CommuterCardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * User-facing AI Hub. Separate from AdminController's fraud/risk detection.
 * Every method operates only on the authenticated user's own data.
 */
@RestController
@RequestMapping("/api/ai-hub")
public class AIHubController {

    private final AIHubService aiHubService;
    private final CommuterCardService cardService;

    public AIHubController(AIHubService aiHubService, CommuterCardService cardService) {
        this.aiHubService = aiHubService;
        this.cardService = cardService;
    }

    @PostMapping("/review-application")
    public ReviewApplicationResponse reviewApplication(@RequestBody ApplicationReviewRequest request) {
        return aiHubService.analyzeApplicationData(request);
    }

    @GetMapping("/application-status/{cardId}")
    public StatusExplanationResponse getApplicationStatus(@PathVariable Long cardId,
                                                            @AuthenticationPrincipal User user) {
        CommuterCard card = cardService.getOwnedCard(cardId, user);
        return aiHubService.explainApplicationStatus(card);
    }

    @GetMapping("/recommendations")
    public RecommendationsResponse getRecommendations(@AuthenticationPrincipal User user) {
        return aiHubService.generateRecommendations(user);
    }

    @GetMapping("/insights")
    public InsightsResponse getInsights(@AuthenticationPrincipal User user) {
        return aiHubService.generateInsights(user);
    }

    @GetMapping("/alerts")
    public AlertsResponse getAlerts(@AuthenticationPrincipal User user) {
        return aiHubService.generateAlerts(user);
    }
}
