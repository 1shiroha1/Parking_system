package com.example.parking.entity;

import com.example.parking.model.RepairTicketStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "repair_ticket")
public class RepairTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reporter_user_id", nullable = false)
    private Long reporterUserId;

    @Column(name = "parking_lot_id")
    private Long parkingLotId;

    @Column(name = "parking_space_id")
    private Long parkingSpaceId;

    @Column(name = "assigned_worker_user_id")
    private Long assignedWorkerUserId;

    @Column(nullable = false, length = 400)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RepairTicketStatus status = RepairTicketStatus.OPEN;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal repairCost = BigDecimal.ZERO;

    @Column(length = 500)
    private String handlerNotes;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public Long getReporterUserId() {
        return reporterUserId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public Long getAssignedWorkerUserId() {
        return assignedWorkerUserId;
    }

    public String getDescription() {
        return description;
    }

    public RepairTicketStatus getStatus() {
        return status;
    }

    public BigDecimal getRepairCost() {
        return repairCost;
    }

    public String getHandlerNotes() {
        return handlerNotes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setReporterUserId(Long reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public void setAssignedWorkerUserId(Long assignedWorkerUserId) {
        this.assignedWorkerUserId = assignedWorkerUserId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(RepairTicketStatus status) {
        this.status = status;
    }

    public void setRepairCost(BigDecimal repairCost) {
        this.repairCost = repairCost;
    }

    public void setHandlerNotes(String handlerNotes) {
        this.handlerNotes = handlerNotes;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

