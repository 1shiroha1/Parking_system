package com.example.parking.repository;

import com.example.parking.entity.Bill;
import com.example.parking.model.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByUserId(Long userId);

    Optional<Bill> findByEntryExitLogId(Long entryExitLogId);

    List<Bill> findByUserIdAndStatus(Long userId, BillStatus status);
}

