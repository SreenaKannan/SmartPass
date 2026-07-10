package com.smartPass.smartpass.repository;

import com.smartPass.smartpass.entity.RenewalHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RenewalHistoryRepository
        extends JpaRepository<RenewalHistory, Long> {

    List<RenewalHistory> findByCardId(Long cardId);
}