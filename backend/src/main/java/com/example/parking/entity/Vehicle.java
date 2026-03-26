package com.example.parking.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "vehicle",
        uniqueConstraints = @UniqueConstraint(name = "uk_vehicle_user_plate", columnNames = {"user_id", "plate_number"})
)
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "plate_number", nullable = false, length = 20)
    private String plateNumber;

    @Column(length = 30)
    private String brand;

    @Column(length = 30)
    private String color;

    @Column(length = 30)
    private String vehicleType;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}

