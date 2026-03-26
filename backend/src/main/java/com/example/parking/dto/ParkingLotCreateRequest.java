package com.example.parking.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ParkingLotCreateRequest {
    @NotBlank
    @Size(max = 100)
    private String name;

    @Size(max = 200)
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

