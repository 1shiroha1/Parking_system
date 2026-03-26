package com.example.parking.entity;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "entry_exit_log")
public class EntryExitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "plate_number", nullable = false, length = 20)
    private String plateNumber;

    @Column(name = "lot_id", nullable = false)
    private Long lotId;

    @Column(name = "space_id", nullable = false)
    private Long spaceId;

    @Column(nullable = false)
    private LocalDateTime entryTime;

    @Column(nullable = false)
    private LocalDateTime exitTime;

    @Column(name = "reservation_id")
    private Long reservationId;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "CLOSED"; // CLOSED, BILLED

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public Long getLotId() {
        return lotId;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public void setEntryTime(LocalDateTime entryTime) {
        this.entryTime = entryTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

