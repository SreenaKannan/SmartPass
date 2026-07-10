package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.service.CommuterCardService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/cards")


public class CommuterCardController {

    private final CommuterCardService cardService;

    public CommuterCardController(
            CommuterCardService cardService) {

        this.cardService = cardService;
    }

    @PostMapping("/create")
    public String createCard(
            @RequestBody CommuterCard card) {

        return cardService.createCard(card);
    }

    @GetMapping
    public List<CommuterCard> getAllCards() {

    return cardService.getAllCards();

    }

    @GetMapping("/{id}")
    public CommuterCard getCardById(@PathVariable Long id) {

    return cardService.getCardById(id);

    }

    @PutMapping("/renew/{id}")
    public String renewCard(
        @PathVariable Long id) {

    return cardService.renewCard(id);

    }

    @DeleteMapping("/{id}")
    public String deleteCard(@PathVariable Long id) {

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
        @RequestBody CommuterCard card) {

    return cardService.updateCard(id, card);
}

@GetMapping("/expiring-soon")
public List<CommuterCard> getExpiringSoonCards() {

    return cardService.getExpiringSoonCards();
}
}

