package com.example.parking.service;

import com.example.parking.common.BusinessException;
import com.example.parking.common.ErrorCode;
import com.example.parking.dto.PaymentDto;
import com.example.parking.entity.Bill;
import com.example.parking.entity.Payment;
import com.example.parking.model.BillStatus;
import com.example.parking.repository.BillRepository;
import com.example.parking.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {
    private final BillRepository billRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(BillRepository billRepository, PaymentRepository paymentRepository) {
        this.billRepository = billRepository;
        this.paymentRepository = paymentRepository;
    }

    public PaymentDto mockPay(Long currentUserId, boolean isAdmin, Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "账单不存在"));
        if (!isAdmin && !bill.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无法支付他人的账单");
        }
        if (bill.getStatus() != BillStatus.UNPAID) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该账单不是待支付状态");
        }

        // 简化支付：直接模拟成功
        String transactionNo = "MOCK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        Payment payment = new Payment();
        payment.setBillId(bill.getId());
        payment.setAmount(bill.getAmount());
        payment.setProvider("MOCK");
        payment.setTransactionNo(transactionNo);
        payment.setStatus("SUCCESS");
        payment.setPaidAt(Instant.now());

        Payment savedPayment = paymentRepository.save(payment);

        bill.setStatus(BillStatus.PAID);
        bill.setPaidAt(savedPayment.getPaidAt());
        billRepository.save(bill);

        PaymentDto dto = new PaymentDto();
        dto.setId(savedPayment.getId());
        dto.setBillId(savedPayment.getBillId());
        dto.setAmount(savedPayment.getAmount());
        dto.setProvider(savedPayment.getProvider());
        dto.setTransactionNo(savedPayment.getTransactionNo());
        dto.setStatus(savedPayment.getStatus());
        dto.setPaidAt(savedPayment.getPaidAt());
        return dto;
    }
}

