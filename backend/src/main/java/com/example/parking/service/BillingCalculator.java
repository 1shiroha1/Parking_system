package com.example.parking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 计费规则：
 * 1) 按小时计费：不足1小时按1小时计算（向上取整）
 * 2) 单价：每小时 3 元
 * 3) 封顶：20 元
 * 4) 无免费时间
 */
public class BillingCalculator {

    private static final BigDecimal HOURLY_PRICE = new BigDecimal("3.00");
    private static final BigDecimal CAP_AMOUNT = new BigDecimal("20.00");
    private static final long MINUTES_PER_HOUR = 60;

    public static BigDecimal calculateAmount(LocalDateTime entryTime, LocalDateTime exitTime) {
        if (entryTime == null || exitTime == null || !exitTime.isAfter(entryTime)) {
            return BigDecimal.ZERO;
        }

        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        // 向上取整到小时；例如 1 分钟也按 1 小时计
        long hours = (minutes + MINUTES_PER_HOUR - 1) / MINUTES_PER_HOUR;
        if (hours <= 0) {
            hours = 1;
        }

        BigDecimal amount = HOURLY_PRICE.multiply(BigDecimal.valueOf(hours)).setScale(2, RoundingMode.HALF_UP);
        if (amount.compareTo(CAP_AMOUNT) > 0) {
            amount = CAP_AMOUNT;
        }
        return amount;
    }
}

