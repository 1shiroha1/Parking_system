package com.example.parking.entity;

import com.example.parking.model.BillStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "entry_exit_log_id", nullable = false, unique = true)
    private Long entryExitLogId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BillStatus status = BillStatus.UNPAID;

    @Column(length = 200)
    private String remark;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column
    private Instant paidAt;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getEntryExitLogId() {
        return entryExitLogId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BillStatus getStatus() {
        return status;
    }

    public String getRemark() {
        return remark;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEntryExitLogId(Long entryExitLogId) {
        this.entryExitLogId = entryExitLogId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }
}

