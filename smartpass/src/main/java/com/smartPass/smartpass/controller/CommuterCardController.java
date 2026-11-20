package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.service.CommuterCardService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.smartPass.smartpass.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
@RestController
@RequestMapping("/api/cards")


public class CommuterCardController {

    private final CommuterCardService cardService;

    public CommuterCardController(
            CommuterCardService cardService) {

        this.cardService = cardService;
    }

    @PostMapping("/create")
    public CommuterCard createCard(@RequestBody CommuterCard card, @AuthenticationPrincipal User user) {

        return cardService.createCard(card, user);
    }

    @GetMapping
    public List<CommuterCard> getAllCards(@AuthenticationPrincipal User user) { return cardService.getMyCards(user); }

    @GetMapping("/{id}")
    public CommuterCard getCardById(@PathVariable Long id, @AuthenticationPrincipal User user) { return cardService.getOwnedCard(id,user); }

    @DeleteMapping("/{id}")
    public String deleteCard(@PathVariable Long id, @AuthenticationPrincipal User user) {
    cardService.getOwnedCard(id,user);

    return cardService.deleteCard(id);

    }
    @PutMapping("/block/{id}")
    public String blockCard(@PathVariable Long id) {

    return cardService.blockCard(id);

    }

    @GetMapping("/expired")
    public List<CommuterCard> getExpiredCards() {

    return cardService.getExpiredCards();

    }

    @GetMapping("/search/{userName}")
    public List<CommuterCard> searchCard(
        @PathVariable String userName) {

    return cardService.searchByUserName(userName);

    }

    @PutMapping("/approve/{id}")
public String approveCard(
        @PathVariable Long id) {

    return cardService.approveCard(id);
}

@PutMapping("/reject/{id}")
public String rejectCard(
        @PathVariable Long id) {

    return cardService.rejectCard(id);
}

    @PutMapping("/update/{id}")
public String updateCard(
        @PathVariable Long id,
        @RequestBody CommuterCard card, @AuthenticationPrincipal User user) {
    cardService.getOwnedCard(id,user);

    return cardService.updateCard(id, card);
}

@GetMapping("/expiring-soon")
public List<CommuterCard> getExpiringSoonCards() {

    return cardService.getExpiringSoonCards();
}
}

