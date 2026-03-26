package com.example.parking.dto;

import com.example.parking.model.RepairTicketStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class RepairTicketDto {
    private Long id;
    private Long reporterUserId;
    private Long parkingLotId;
    private Long parkingSpaceId;
    private Long assignedWorkerUserId;
    private String description;
    private RepairTicketStatus status;
    private BigDecimal repairCost;
    private String handlerNotes;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReporterUserId() {
        return reporterUserId;
    }

    public void setReporterUserId(Long reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public Long getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(Long parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public Long getParkingSpaceId() {
        return parkingSpaceId;
    }

    public void setParkingSpaceId(Long parkingSpaceId) {
        this.parkingSpaceId = parkingSpaceId;
    }

    public Long getAssignedWorkerUserId() {
        return assignedWorkerUserId;
    }

    public void setAssignedWorkerUserId(Long assignedWorkerUserId) {
        this.assignedWorkerUserId = assignedWorkerUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RepairTicketStatus getStatus() {
        return status;
    }

    public void setStatus(RepairTicketStatus status) {
        this.status = status;
    }

    public BigDecimal getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(BigDecimal repairCost) {
        this.repairCost = repairCost;
    }

    public String getHandlerNotes() {
        return handlerNotes;
    }

    public void setHandlerNotes(String handlerNotes) {
        this.handlerNotes = handlerNotes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

