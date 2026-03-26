package com.example.parking.dto;

import javax.validation.constraints.Size;

public class ReservationCancelRequest {
    @Size(max = 200)
    private String cancelReason;

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}

