package com.example.parking.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "repair_worker_profile", indexes = {
        @Index(name = "idx_worker_user_id", columnList = "user_id")
})
public class RepairWorkerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, length = 80)
    private String workerName;

    @Column(length = 30)
    private String phone;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getPhone() {
        return phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

