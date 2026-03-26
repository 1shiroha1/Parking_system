package com.example.parking.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RepairTicketCreateRequest {
    private Long parkingLotId;
    private Long parkingSpaceId;
    private Long assignedWorkerUserId;

    @NotNull
    @Size(min = 5, max = 400)
    private String description;

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
}

