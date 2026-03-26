package com.example.parking.repository;

import com.example.parking.entity.RepairTicket;
import com.example.parking.model.RepairTicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairTicketRepository extends JpaRepository<RepairTicket, Long> {
    List<RepairTicket> findByAssignedWorkerUserId(Long assignedWorkerUserId);

    List<RepairTicket> findByReporterUserId(Long reporterUserId);

    List<RepairTicket> findByParkingSpaceId(Long parkingSpaceId);
}

