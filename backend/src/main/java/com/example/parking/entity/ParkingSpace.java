package com.example.parking.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(
        name = "parking_space",
        uniqueConstraints = @UniqueConstraint(name = "uk_space_lot_code", columnNames = {"lot_id", "code"})
)
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_id", nullable = false)
    private Long lotId;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false, length = 20)
    private String status = "AVAILABLE"; // AVAILABLE, REPAIR

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Long getId() {
        return id;
    }

    public Long getLotId() {
        return lotId;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

