package com.example.parking.service;

import com.example.parking.common.BusinessException;
import com.example.parking.common.ErrorCode;
import com.example.parking.dto.EntryExitLogCreateRequest;
import com.example.parking.dto.EntryExitLogDto;
import com.example.parking.entity.Bill;
import com.example.parking.entity.EntryExitLog;
import com.example.parking.entity.ParkingSpace;
import com.example.parking.entity.Vehicle;
import com.example.parking.model.BillStatus;
import com.example.parking.repository.BillRepository;
import com.example.parking.repository.EntryExitLogRepository;
import com.example.parking.repository.ParkingSpaceRepository;
import com.example.parking.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EntryExitLogService {
    private final VehicleRepository vehicleRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final EntryExitLogRepository entryExitLogRepository;
    private final BillRepository billRepository;

    public EntryExitLogService(VehicleRepository vehicleRepository,
                                 ParkingSpaceRepository parkingSpaceRepository,
                                 EntryExitLogRepository entryExitLogRepository,
                                 BillRepository billRepository) {
        this.vehicleRepository = vehicleRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.entryExitLogRepository = entryExitLogRepository;
        this.billRepository = billRepository;
    }

    public EntryExitLogDto createLog(Long adminUserId, EntryExitLogCreateRequest req) {
        LocalDateTime entryTime = req.getEntryTime();
        LocalDateTime exitTime = req.getExitTime();
        if (exitTime == null || entryTime == null || !exitTime.isAfter(entryTime)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "出场时间必须晚于入场时间");
        }

        Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "车辆不存在"));
        ParkingSpace space = parkingSpaceRepository.findById(req.getSpaceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "车位不存在"));

        // 避免同一车位时间段重叠（管理员手动录入，用强校验更安全）
        boolean hasOverlap = !entryExitLogRepository.findOverlapping(req.getSpaceId(), entryTime, exitTime).isEmpty();
        if (hasOverlap) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "该车位在该时间段已有记录");
        }

        // 计算费用并创建账单
        BigDecimal amount = BillingCalculator.calculateAmount(entryTime, exitTime);

        EntryExitLog log = new EntryExitLog();
        log.setUserId(vehicle.getUserId());
        log.setVehicleId(vehicle.getId());
        log.setPlateNumber(vehicle.getPlateNumber());
        log.setLotId(space.getLotId());
        log.setSpaceId(space.getId());
        log.setEntryTime(entryTime);
        log.setExitTime(exitTime);
        log.setReservationId(req.getReservationId());
        log.setStatus("CLOSED");
        entryExitLogRepository.save(log);

        Bill bill = new Bill();
        bill.setUserId(vehicle.getUserId());
        bill.setEntryExitLogId(log.getId());
        bill.setAmount(amount);
        bill.setStatus(BillStatus.UNPAID);
        billRepository.save(bill);

        EntryExitLogDto dto = new EntryExitLogDto();
        dto.setId(log.getId());
        dto.setUserId(log.getUserId());
        dto.setVehicleId(log.getVehicleId());
        dto.setSpaceId(log.getSpaceId());
        dto.setPlateNumber(log.getPlateNumber());
        dto.setLotId(log.getLotId());
        dto.setEntryTime(log.getEntryTime());
        dto.setExitTime(log.getExitTime());
        dto.setReservationId(log.getReservationId());
        dto.setBillId(bill.getId());
        dto.setBillStatus(bill.getStatus().name());
        dto.setAmount(bill.getAmount().toPlainString());
        return dto;
    }
}

