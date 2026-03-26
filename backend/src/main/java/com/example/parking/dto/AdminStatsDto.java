package com.example.parking.dto;

import java.util.List;
import java.util.Map;

public class AdminStatsDto {
    private int days;

    private int parkingSpaceCount;

    // utilization: 0~100 (%)
    private double utilizationRate;

    private List<DayAmountDto> revenueByDay;
    private List<DayCountDto> reservationByDay;

    private Map<String, Long> repairTicketByStatus;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getParkingSpaceCount() {
        return parkingSpaceCount;
    }

    public void setParkingSpaceCount(int parkingSpaceCount) {
        this.parkingSpaceCount = parkingSpaceCount;
    }

    public double getUtilizationRate() {
        return utilizationRate;
    }

    public void setUtilizationRate(double utilizationRate) {
        this.utilizationRate = utilizationRate;
    }

    public List<DayAmountDto> getRevenueByDay() {
        return revenueByDay;
    }

    public void setRevenueByDay(List<DayAmountDto> revenueByDay) {
        this.revenueByDay = revenueByDay;
    }

    public List<DayCountDto> getReservationByDay() {
        return reservationByDay;
    }

    public void setReservationByDay(List<DayCountDto> reservationByDay) {
        this.reservationByDay = reservationByDay;
    }

    public Map<String, Long> getRepairTicketByStatus() {
        return repairTicketByStatus;
    }

    public void setRepairTicketByStatus(Map<String, Long> repairTicketByStatus) {
        this.repairTicketByStatus = repairTicketByStatus;
    }
}

