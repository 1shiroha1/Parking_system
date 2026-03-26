package com.example.parking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_id", nullable = false)
    private Long billId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    private String provider; // MOCK

    @Column(name = "transaction_no", nullable = false, length = 100)
    private String transactionNo;

    @Column(nullable = false, length = 20)
    private String status; // SUCCESS, FAILED

    @Column
    private Instant paidAt;

    public Long getId() {
        return id;
    }

    public Long getBillId() {
        return billId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getProvider() {
        return provider;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public String getStatus() {
        return status;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }
}

