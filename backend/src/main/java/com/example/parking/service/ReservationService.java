package com.example.parking.service;

import com.example.parking.common.BusinessException;
import com.example.parking.common.ErrorCode;
import com.example.parking.dto.ReservationCreateRequest;
import com.example.parking.entity.ParkingSpace;
import com.example.parking.entity.Reservation;
import com.example.parking.repository.ParkingSpaceRepository;
import com.example.parking.repository.ReservationRepository;
import com.example.parking.model.ReservationStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;

    public ReservationService(ReservationRepository reservationRepository,
                               ParkingSpaceRepository parkingSpaceRepository) {
        this.reservationRepository = reservationRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public Reservation createReservation(Long userId, ReservationCreateRequest req) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = req.getStartTime();
        LocalDateTime end = req.getEndTime();

        if (start.isBefore(now)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "预约开始时间不能早于当前时间");
        }
        if (start.isAfter(now.plusDays(1))) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "只能提前一天预约");
        }
        if (!end.isAfter(start)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "结束时间必须晚于开始时间");
        }
        Duration d = Duration.between(start, end);
        if (d.compareTo(Duration.ofDays(1)) > 0) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "一次最多预约一天");
        }

        ParkingSpace space = parkingSpaceRepository.findById(req.getSpaceId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "车位不存在"));
        if ("REPAIR".equalsIgnoreCase(space.getStatus())) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "该车位正在维修，暂不可预约");
        }

        List<ReservationStatus> active = Arrays.asList(ReservationStatus.CONFIRMED);
        List<Reservation> overlap = reservationRepository.findOverlapping(
                req.getSpaceId(),
                start,
                end,
                active
        );
        if (!overlap.isEmpty()) {
            throw new BusinessException(ErrorCode.BUSINESS_ERROR, "车位预约冲突：该时间段已被预约");
        }

        Reservation r = new Reservation();
        r.setUserId(userId);
        r.setSpaceId(req.getSpaceId());
        r.setStartTime(start);
        r.setEndTime(end);
        r.setStatus(ReservationStatus.CONFIRMED);
        r.setCancelReason(null);
        return reservationRepository.save(r);
    }

    public Reservation cancelReservation(Long userId, Long reservationId, String cancelReason) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "预约不存在"));
        if (!r.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "无法取消他人的预约");
        }
        if (r.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该预约已取消");
        }
        r.setStatus(ReservationStatus.CANCELLED);
        r.setCancelReason(cancelReason);
        return reservationRepository.save(r);
    }

    public List<Reservation> listMy(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> listAll() {
        return reservationRepository.findAll();
    }
}

