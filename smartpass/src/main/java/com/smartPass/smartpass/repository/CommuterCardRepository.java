package com.smartPass.smartpass.repository;

import com.smartPass.smartpass.entity.CommuterCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CommuterCardRepository
        extends JpaRepository<CommuterCard, Long> {

    List<CommuterCard> findByExpiryDateBefore(LocalDate date);

    long countByStatus(String status);

    List<CommuterCard> findByUserName(String userName);
    List<CommuterCard> findByExpiryDateBetween(
        LocalDate startDate,
        LocalDate endDate);

}