package com.example.parking.service;

import com.example.parking.common.BusinessException;
import com.example.parking.common.ErrorCode;
import com.example.parking.dto.ParkingLotCreateRequest;
import com.example.parking.dto.ParkingLotDto;
import com.example.parking.dto.ParkingSpaceCreateRequest;
import com.example.parking.dto.ParkingSpaceDto;
import com.example.parking.entity.EntryExitLog;
import com.example.parking.entity.ParkingLot;
import com.example.parking.entity.ParkingSpace;
import com.example.parking.repository.EntryExitLogRepository;
import com.example.parking.repository.ParkingLotRepository;
import com.example.parking.repository.ParkingSpaceRepository;
import com.example.parking.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingService {
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ReservationRepository reservationRepository;
    private final EntryExitLogRepository entryExitLogRepository;

    public ParkingService(ParkingLotRepository parkingLotRepository,
                           ParkingSpaceRepository parkingSpaceRepository,
                           ReservationRepository reservationRepository,
                           EntryExitLogRepository entryExitLogRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.reservationRepository = reservationRepository;
        this.entryExitLogRepository = entryExitLogRepository;
    }

    public List<ParkingLotDto> listLots() {
        return parkingLotRepository.findAll().stream().map(this::toLotDto).collect(Collectors.toList());
    }

    public List<ParkingSpaceDto> listSpaces(Long lotId) {
        LocalDateTime now = LocalDateTime.now();
        return parkingSpaceRepository.findByLotId(lotId).stream().map(space -> toSpaceDto(space, now)).collect(Collectors.toList());
    }

    private ParkingLotDto toLotDto(ParkingLot lot) {
        ParkingLotDto dto = new ParkingLotDto();
        dto.setId(lot.getId());
        dto.setName(lot.getName());
        dto.setAddress(lot.getAddress());
        dto.setStatus(lot.getStatus());
        return dto;
    }

    private ParkingSpaceDto toSpaceDto(ParkingSpace space, LocalDateTime now) {
        ParkingSpaceDto dto = new ParkingSpaceDto();
        dto.setId(space.getId());
        dto.setLotId(space.getLotId());
        dto.setCode(space.getCode());

        String statusNow;
        if ("REPAIR".equalsIgnoreCase(space.getStatus())) {
            statusNow = "REPAIR";
        } else {
            List<EntryExitLog> occupying = entryExitLogRepository.findOccupyingNow(space.getId(), now);
            if (!occupying.isEmpty()) {
                statusNow = "OCCUPIED";
            } else {
                boolean hasReserved = !reservationRepository.findReservedNow(space.getId(), now).isEmpty();
                statusNow = hasReserved ? "RESERVED" : "AVAILABLE";
            }
        }
        dto.setStatusNow(statusNow);
        return dto;
    }

    public ParkingLot createLot(ParkingLotCreateRequest req) {
        ParkingLot lot = new ParkingLot();
        lot.setName(req.getName());
        lot.setAddress(req.getAddress());
        lot.setStatus("OPEN");
        return parkingLotRepository.save(lot);
    }

    public ParkingSpace createSpace(ParkingSpaceCreateRequest req) {
        ParkingSpace space = new ParkingSpace();
        space.setLotId(req.getLotId());
        space.setCode(req.getCode());
        space.setStatus("AVAILABLE");
        return parkingSpaceRepository.save(space);
    }

    public ParkingSpace updateSpaceRepairStatus(Long spaceId, String status) {
        ParkingSpace space = parkingSpaceRepository.findById(spaceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "车位不存在"));
        if ("REPAIR".equalsIgnoreCase(status) || "AVAILABLE".equalsIgnoreCase(status)) {
            space.setStatus(status.toUpperCase());
        } else {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "状态只能为 REPAIR 或 AVAILABLE");
        }
        return parkingSpaceRepository.save(space);
    }
}

