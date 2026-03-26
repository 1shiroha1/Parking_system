package com.example.parking.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ParkingSpaceCreateRequest {
    @NotNull
    private Long lotId;

    @NotNull
    @Size(min = 1, max = 20)
    private String code;

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

