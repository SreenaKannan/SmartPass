package com.smartPass.smartpass.service;

import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.repository.CommuterCardRepository;
import com.smartPass.smartpass.repository.PaymentRepository;
import com.smartPass.smartpass.repository.RenewalHistoryRepository;
import com.smartPass.smartpass.entity.Payment;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;
import com.smartPass.smartpass.entity.RenewalHistory;
import com.smartPass.smartpass.repository.RenewalHistoryRepository;

@Service
public class CommuterCardService {

    private final CommuterCardRepository cardRepository;
    private final PaymentRepository paymentRepository;
    private final RenewalHistoryRepository renewalRepository;

   public CommuterCardService(
        CommuterCardRepository cardRepository,
        PaymentRepository paymentRepository,
        RenewalHistoryRepository renewalRepository) {

    this.cardRepository = cardRepository;
    this.paymentRepository = paymentRepository;
    this.renewalRepository = renewalRepository;
}

    public CommuterCard getCardById(Long id) {

    return cardRepository.findById(id).orElse(null);

    }

    public String createCard(CommuterCard card) {

    card.setStatus("Active");

    // Automatically set today's date
    card.setIssueDate(LocalDate.now().toString());

    if(card.getPassType().equalsIgnoreCase("Monthly")) {

        card.setFareAmount(500);
        card.setExpiryDate(LocalDate.now().plusMonths(1));

    }
    else if(card.getPassType().equalsIgnoreCase("Quarterly")) {

        card.setFareAmount(1200);
        card.setExpiryDate(LocalDate.now().plusMonths(3));

    }
    else if(card.getPassType().equalsIgnoreCase("Half-Yearly")) {

        card.setFareAmount(2200);
        card.setExpiryDate(LocalDate.now().plusMonths(6));

    }
    else if(card.getPassType().equalsIgnoreCase("Yearly")) {

        card.setFareAmount(4500);
        card.setExpiryDate(LocalDate.now().plusYears(1));

    }

    cardRepository.save(card);

    return "Card created successfully";
}

    public List<CommuterCard> getAllCards() {
    return cardRepository.findAll();
    }

    public String renewCard(Long id) {

    List<Payment> payments =
            paymentRepository.findByCardId(id);

    if (payments.isEmpty()) {
        return "Please complete payment first";
    }

    CommuterCard card =
            cardRepository.findById(id).orElse(null);

    if (card == null) {
        return "Card not found";
    }

    RenewalHistory history = new RenewalHistory();

history.setCardId(card.getCardId());

history.setUserName(card.getUserName());

history.setRenewalDate(LocalDate.now().toString());

history.setOldExpiryDate(card.getExpiryDate().toString());

card.setExpiryDate(card.getExpiryDate().plusMonths(1));

history.setNewExpiryDate(card.getExpiryDate().toString());

history.setAmountPaid(card.getFareAmount());

renewalRepository.save(history);
    card.setStatus("Renewed");

    cardRepository.save(card);

    return "Card renewed successfully";
}

    public String deleteCard(Long id) {

    if (cardRepository.existsById(id)) {

        cardRepository.deleteById(id);

        return "Card deleted successfully";
    }

    return "Card not found";
    }

    public String blockCard(Long id) {

    CommuterCard card =
            cardRepository.findById(id).orElse(null);

    if (card != null) {

        card.setStatus("Blocked");

        cardRepository.save(card);

        return "Card blocked successfully";
    }

    return "Card not found";
    }

    public List<CommuterCard> getExpiredCards() {

    return cardRepository.findByExpiryDateBefore(
            LocalDate.now());

    }

    public List<CommuterCard> searchByUserName(String userName) {

    return cardRepository.findByUserName(userName);

    }

public String approveCard(Long id) {

    CommuterCard card =
            cardRepository.findById(id).orElse(null);

    if(card == null) {
        return "Card not found";
    }

    card.setStatus("Approved");

    cardRepository.save(card);

    return "Card approved successfully";
}
    public String rejectCard(Long id) {

    CommuterCard card =
            cardRepository.findById(id).orElse(null);

    if(card == null) {
        return "Card not found";
    }

    card.setStatus("Rejected");

    cardRepository.save(card);

    return "Card rejected successfully";
}

public String updateCard(Long id,
                         CommuterCard updatedCard) {

    CommuterCard card =
            cardRepository.findById(id).orElse(null);

    if (card == null) {
        return "Card not found";
    }

    card.setSourceStation(
            updatedCard.getSourceStation());

    card.setDestinationStation(
            updatedCard.getDestinationStation());

    card.setPassType(
            updatedCard.getPassType());

    card.setFareAmount(
            updatedCard.getFareAmount());

    cardRepository.save(card);

    return "Card updated successfully";
}

public List<CommuterCard> getExpiringSoonCards() {

    return cardRepository.findByExpiryDateBetween(
            LocalDate.now(),
            LocalDate.now().plusDays(7));
}
}