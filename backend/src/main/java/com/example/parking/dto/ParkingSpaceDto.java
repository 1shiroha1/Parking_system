package com.example.parking.dto;

public class ParkingSpaceDto {
    private Long id;
    private Long lotId;
    private String code;
    private String statusNow;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStatusNow() {
        return statusNow;
    }

    public void setStatusNow(String statusNow) {
        this.statusNow = statusNow;
    }
}

