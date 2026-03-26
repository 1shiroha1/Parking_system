package com.example.parking.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class VehicleCreateRequest {
    @NotBlank
    @Size(max = 20)
    private String plateNumber;

    @Size(max = 30)
    private String brand;

    @Size(max = 30)
    private String color;

    @Size(max = 30)
    private String vehicleType;

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}

