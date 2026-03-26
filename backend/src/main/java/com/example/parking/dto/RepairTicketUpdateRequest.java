package com.example.parking.dto;

import com.example.parking.model.RepairTicketStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class RepairTicketUpdateRequest {
    @NotNull
    private RepairTicketStatus status;

    private BigDecimal repairCost;

    @Size(max = 500)
    private String handlerNotes;

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
}

