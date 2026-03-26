package com.example.parking.service;

import com.example.parking.dto.BillDto;
import com.example.parking.entity.Bill;
import com.example.parking.entity.EntryExitLog;
import com.example.parking.repository.BillRepository;
import com.example.parking.repository.EntryExitLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final EntryExitLogRepository entryExitLogRepository;

    public BillService(BillRepository billRepository, EntryExitLogRepository entryExitLogRepository) {
        this.billRepository = billRepository;
        this.entryExitLogRepository = entryExitLogRepository;
    }

    public List<BillDto> listBills(Long currentUserId, boolean isAdmin) {
        List<Bill> bills = isAdmin ? billRepository.findAll() : billRepository.findByUserId(currentUserId);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return bills.stream().map(b -> {
            EntryExitLog log = entryExitLogRepository.findById(b.getEntryExitLogId()).orElse(null);
            BillDto dto = new BillDto();
            dto.setId(b.getId());
            dto.setUserId(b.getUserId());
            dto.setEntryExitLogId(b.getEntryExitLogId());
            dto.setAmount(b.getAmount());
            dto.setStatus(b.getStatus().name());
            dto.setCreatedAt(b.getCreatedAt());
            dto.setPaidAt(b.getPaidAt());
            if (log != null) {
                dto.setPlateNumber(log.getPlateNumber());
                dto.setSpaceId(log.getSpaceId());
                LocalDateTime et = log.getEntryTime();
                LocalDateTime xt = log.getExitTime();
                dto.setEntryTime(et == null ? null : et.format(fmt));
                dto.setExitTime(xt == null ? null : xt.format(fmt));
            }
            return dto;
        }).collect(Collectors.toList());
    }
}

