package com.example.parking.service;

import com.example.parking.dto.AdminStatsDto;
import com.example.parking.dto.DayAmountDto;
import com.example.parking.dto.DayCountDto;
import com.example.parking.entity.Bill;
import com.example.parking.entity.EntryExitLog;
import com.example.parking.entity.ParkingSpace;
import com.example.parking.entity.RepairTicket;
import com.example.parking.model.BillStatus;
import com.example.parking.model.ReservationStatus;
import com.example.parking.model.RepairTicketStatus;
import com.example.parking.repository.BillRepository;
import com.example.parking.repository.EntryExitLogRepository;
import com.example.parking.repository.ParkingSpaceRepository;
import com.example.parking.repository.RepairTicketRepository;
import com.example.parking.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminStatsService {

    private final BillRepository billRepository;
    private final ReservationRepository reservationRepository;
    private final RepairTicketRepository repairTicketRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final EntryExitLogRepository entryExitLogRepository;

    public AdminStatsService(BillRepository billRepository,
                              ReservationRepository reservationRepository,
                              RepairTicketRepository repairTicketRepository,
                              ParkingSpaceRepository parkingSpaceRepository,
                              EntryExitLogRepository entryExitLogRepository) {
        this.billRepository = billRepository;
        this.reservationRepository = reservationRepository;
        this.repairTicketRepository = repairTicketRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.entryExitLogRepository = entryExitLogRepository;
    }

    public AdminStatsDto getAdminStats(int days) {
        if (days <= 0) days = 7;
        if (days > 30) days = 30;

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days - 1);
        LocalDateTime rangeStart = startDate.atStartOfDay();
        LocalDateTime rangeEnd = rangeStart.plusDays(days);

        List<ParkingSpace> spaces = parkingSpaceRepository.findAll();
        int spaceCount = spaces.size();

        // 1) revenueByDay (paid bills)
        List<Bill> bills = billRepository.findAll();
        List<DayAmountDto> revenueByDay = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate d = startDate.plusDays(i);
            LocalDateTime dayStart = d.atStartOfDay();
            LocalDateTime dayEnd = dayStart.plusDays(1);

            BigDecimal sum = BigDecimal.ZERO;
            for (Bill b : bills) {
                if (b.getStatus() != BillStatus.PAID) continue;
                if (b.getPaidAt() == null) continue;
                LocalDateTime paidAt = LocalDateTime.ofInstant(b.getPaidAt(), java.time.ZoneId.systemDefault());
                if (!paidAt.isBefore(dayStart) && paidAt.isBefore(dayEnd)) {
                    if (b.getAmount() != null) sum = sum.add(b.getAmount());
                }
            }

            DayAmountDto dto = new DayAmountDto();
            dto.setDate(d.toString());
            dto.setAmount(sum.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            revenueByDay.add(dto);
        }

        // 2) reservationByDay
        List<DayCountDto> reservationByDay = new ArrayList<>();
        List<com.example.parking.entity.Reservation> reservations = reservationRepository.findAll();
        for (int i = 0; i < days; i++) {
            LocalDate d = startDate.plusDays(i);
            LocalDateTime dayStart = d.atStartOfDay();
            LocalDateTime dayEnd = dayStart.plusDays(1);

            long count = 0;
            for (com.example.parking.entity.Reservation r : reservations) {
                if (r.getStatus() != ReservationStatus.CONFIRMED) continue;
                LocalDateTime st = r.getStartTime();
                if (st == null) continue;
                if (!st.isBefore(dayStart) && st.isBefore(dayEnd)) {
                    count++;
                }
            }

            DayCountDto dto = new DayCountDto();
            dto.setDate(d.toString());
            dto.setCount(count);
            reservationByDay.add(dto);
        }

        // 3) repairTicketByStatus
        List<RepairTicket> tickets = repairTicketRepository.findAll();
        Map<String, Long> repairSummary = new HashMap<>();
        for (RepairTicketStatus s : RepairTicketStatus.values()) {
            repairSummary.put(s.name(), 0L);
        }
        for (RepairTicket t : tickets) {
            if (t.getStatus() == null) continue;
            repairSummary.put(t.getStatus().name(), repairSummary.get(t.getStatus().name()) + 1);
        }

        // 4) utilizationRate (based on entry/exit logs duration overlap)
        // utilizationRate = totalOccupiedMinutes / (spaceCount * totalRangeMinutes) * 100
        long totalRangeMinutes = Duration.between(rangeStart, rangeEnd).toMinutes();
        long totalOccupiedMinutes = 0;
        if (spaceCount > 0 && totalRangeMinutes > 0) {
            List<EntryExitLog> logs = entryExitLogRepository.findAll();
            for (EntryExitLog log : logs) {
                LocalDateTime entry = log.getEntryTime();
                LocalDateTime exit = log.getExitTime();
                if (entry == null || exit == null) continue;
                if (!exit.isAfter(rangeStart) || !entry.isBefore(rangeEnd)) {
                    continue; // no overlap
                }
                LocalDateTime overlapStart = entry.isAfter(rangeStart) ? entry : rangeStart;
                LocalDateTime overlapEnd = exit.isBefore(rangeEnd) ? exit : rangeEnd;
                long overlapMinutes = Duration.between(overlapStart, overlapEnd).toMinutes();
                if (overlapMinutes > 0) totalOccupiedMinutes += overlapMinutes;
            }
        }

        double utilizationRate = 0;
        if (spaceCount > 0 && totalRangeMinutes > 0) {
            utilizationRate = (double) totalOccupiedMinutes / (double) (spaceCount * totalRangeMinutes) * 100.0;
        }

        AdminStatsDto dto = new AdminStatsDto();
        dto.setDays(days);
        dto.setParkingSpaceCount(spaceCount);
        dto.setUtilizationRate(Math.round(utilizationRate * 100.0) / 100.0);
        dto.setRevenueByDay(revenueByDay);
        dto.setReservationByDay(reservationByDay);
        dto.setRepairTicketByStatus(repairSummary);
        return dto;
    }
}

