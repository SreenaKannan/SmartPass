package com.smartPass.smartpass.service;

import com.smartPass.smartpass.entity.CommuterCard;
import com.smartPass.smartpass.repository.CommuterCardRepository;
import com.smartPass.smartpass.repository.PaymentRepository;
import com.smartPass.smartpass.repository.RenewalHistoryRepository;
import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.dto.RiskAssessmentDTO;
import com.smartPass.smartpass.repository.RiskAlertRepository;
import com.smartPass.smartpass.entity.RiskAlert;
import com.smartPass.smartpass.entity.Payment;
import com.smartPass.smartpass.entity.RenewalHistory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDate;

@Service
public class CommuterCardService {

    private final CommuterCardRepository cardRepository;
    private final PaymentRepository paymentRepository;
    private final RenewalHistoryRepository renewalRepository;
    private final RiskAnalysisService riskAnalysisService;
    private final RiskAlertRepository riskAlertRepository;

   public CommuterCardService(
        CommuterCardRepository cardRepository,
        PaymentRepository paymentRepository,
        RenewalHistoryRepository renewalRepository, 
        RiskAnalysisService riskAnalysisService, 
        RiskAlertRepository riskAlertRepository) {

    this.cardRepository = cardRepository;
    this.paymentRepository = paymentRepository;
    this.renewalRepository = renewalRepository;
    this.riskAnalysisService = riskAnalysisService;
    this.riskAlertRepository = riskAlertRepository;
}

    public CommuterCard getCardById(Long id) {

    return cardRepository.findById(id).orElse(null);

    }

    public CommuterCard createCard(CommuterCard card, User owner) {

    card.setStatus("Pending");
    card.setOwnerId(owner.getId());
    card.setUserName(owner.getName());

    // Automatically set today's date
    card.setIssueDate(LocalDate.now().toString());

    if(card.getPassType().equalsIgnoreCase("Monthly")) {

        card.setFareAmount(50);
        card.setExpiryDate(LocalDate.now().plusMonths(1));

    }
    else if(card.getPassType().equalsIgnoreCase("Quarterly")) {

        card.setFareAmount(125);
        card.setExpiryDate(LocalDate.now().plusMonths(3));

    }
    else if(card.getPassType().equalsIgnoreCase("Half-Yearly")) {

        card.setFareAmount(250);
        card.setExpiryDate(LocalDate.now().plusMonths(6));

    }
    else if(card.getPassType().equalsIgnoreCase("Yearly")) {

        card.setFareAmount(500);
        card.setExpiryDate(LocalDate.now().plusYears(1));

    }

    cardRepository.save(card);
    try {
      RiskAssessmentDTO assessment = riskAnalysisService.analyze(card, owner);
      card.setRiskScore(assessment.getRiskScore()); card.setRiskLevel(assessment.getRiskLevel());
      card.setRiskFlags(String.join(" | ", assessment.getFlags())); card.setRiskExplanation(assessment.getExplanation());
      cardRepository.save(card);
      if (!"LOW".equals(assessment.getRiskLevel())) { RiskAlert alert=new RiskAlert(); alert.setCardId(card.getCardId()); alert.setRiskScore(assessment.getRiskScore()); alert.setRiskLevel(assessment.getRiskLevel()); alert.setReason(card.getRiskFlags()); riskAlertRepository.save(alert); }
    } catch (Exception ignored) { card.setRiskLevel("UNAVAILABLE"); card.setRiskScore(0); card.setRiskExplanation("AI analysis unavailable. Manual review required."); cardRepository.save(card); }
    return card;
}

    public List<CommuterCard> getMyCards(User user) { return cardRepository.findByOwnerId(user.getId()); }
    public CommuterCard getOwnedCard(Long id, User user) { CommuterCard c=getCardById(id); if(c==null || !user.getId().equals(c.getOwnerId())) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN); return c; }

    public List<CommuterCard> getAllCards() {
    return cardRepository.findAll();
    }

    /**
     * Called only by PaymentService after a SUCCESSFUL renewal payment.
     * Extends the pass by its own duration (monthly/quarterly/half-yearly/yearly),
     * starting from the current expiry, or from today if it has already lapsed.
     */
    public String renewCard(Long id) {

        CommuterCard card = cardRepository.findById(id).orElse(null);

        if (card == null) {
            return "Card not found";
        }

        LocalDate today = LocalDate.now();
        LocalDate oldExpiry = card.getExpiryDate();
        LocalDate base = (oldExpiry != null && oldExpiry.isAfter(today)) ? oldExpiry : today;
        LocalDate newExpiry = extendByPassType(base, card.getPassType());

        RenewalHistory history = new RenewalHistory();
        history.setCardId(card.getCardId());
        history.setUserName(card.getUserName());
        history.setRenewalDate(today.toString());
        history.setOldExpiryDate(oldExpiry == null ? "-" : oldExpiry.toString());
        history.setNewExpiryDate(newExpiry.toString());
        history.setAmountPaid(card.getFareAmount());
        renewalRepository.save(history);

        card.setExpiryDate(newExpiry);
        card.setStatus("Approved");
        cardRepository.save(card);

        return "Card renewed successfully";
    }

    private LocalDate extendByPassType(LocalDate base, String passType) {
        String type = passType == null ? "" : passType.trim().toLowerCase();
        switch (type) {
            case "quarterly":   return base.plusMonths(3);
            case "half-yearly": return base.plusMonths(6);
            case "yearly":      return base.plusYears(1);
            default:            return base.plusMonths(1);
        }
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
