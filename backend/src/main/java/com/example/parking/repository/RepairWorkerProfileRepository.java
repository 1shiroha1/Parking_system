package com.example.parking.repository;

import com.example.parking.entity.RepairWorkerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepairWorkerProfileRepository extends JpaRepository<RepairWorkerProfile, Long> {
    Optional<RepairWorkerProfile> findByUserId(Long userId);
}

