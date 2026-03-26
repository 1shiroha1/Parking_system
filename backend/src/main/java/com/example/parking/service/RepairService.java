package com.example.parking.service;

import com.example.parking.common.BusinessException;
import com.example.parking.common.ErrorCode;
import com.example.parking.dto.RepairTicketCreateRequest;
import com.example.parking.dto.RepairTicketDto;
import com.example.parking.dto.RepairTicketUpdateRequest;
import com.example.parking.dto.RepairWorkerCreateRequest;
import com.example.parking.entity.ParkingSpace;
import com.example.parking.entity.RepairTicket;
import com.example.parking.entity.RepairWorkerProfile;
import com.example.parking.entity.SysUser;
import com.example.parking.model.RepairTicketStatus;
import com.example.parking.model.UserRole;
import com.example.parking.repository.ParkingSpaceRepository;
import com.example.parking.repository.RepairTicketRepository;
import com.example.parking.repository.RepairWorkerProfileRepository;
import com.example.parking.repository.SysUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepairService {
    private final SysUserRepository sysUserRepository;
    private final RepairWorkerProfileRepository workerProfileRepository;
    private final RepairTicketRepository repairTicketRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final PasswordEncoder passwordEncoder;

    public RepairService(SysUserRepository sysUserRepository,
                          RepairWorkerProfileRepository workerProfileRepository,
                          RepairTicketRepository repairTicketRepository,
                          ParkingSpaceRepository parkingSpaceRepository,
                          PasswordEncoder passwordEncoder) {
        this.sysUserRepository = sysUserRepository;
        this.workerProfileRepository = workerProfileRepository;
        this.repairTicketRepository = repairTicketRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createRepairWorker(RepairWorkerCreateRequest req) {
        sysUserRepository.findByUsername(req.getUsername()).ifPresent(u -> {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "维修人员账号已存在");
        });

        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.REPAIR);
        user.setDisplayName(req.getWorkerName());
        SysUser saved = sysUserRepository.save(user);

        RepairWorkerProfile profile = new RepairWorkerProfile();
        profile.setUserId(saved.getId());
        profile.setWorkerName(req.getWorkerName());
        profile.setPhone(req.getPhone());
        workerProfileRepository.save(profile);
    }

    public RepairTicketDto createTicket(Long reporterUserId, RepairTicketCreateRequest req) {
        if (req.getParkingSpaceId() == null && req.getParkingLotId() == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "必须指定停车位或停车场");
        }

        RepairTicket ticket = new RepairTicket();
        ticket.setReporterUserId(reporterUserId);
        ticket.setParkingLotId(req.getParkingLotId());
        ticket.setParkingSpaceId(req.getParkingSpaceId());
        ticket.setAssignedWorkerUserId(req.getAssignedWorkerUserId());
        ticket.setDescription(req.getDescription());
        ticket.setStatus(RepairTicketStatus.OPEN);
        ticket.setRepairCost(java.math.BigDecimal.ZERO);
        ticket.setHandlerNotes(null);

        RepairTicket saved = repairTicketRepository.save(ticket);

        if (req.getParkingSpaceId() != null) {
            ParkingSpace space = parkingSpaceRepository.findById(req.getParkingSpaceId())
                    .orElse(null);
            if (space != null) {
                space.setStatus("REPAIR");
                parkingSpaceRepository.save(space);
            }
        }

        return toDto(saved);
    }

    public List<RepairTicketDto> listMyAssigned(Long workerUserId) {
        return repairTicketRepository.findByAssignedWorkerUserId(workerUserId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<RepairTicketDto> listAll() {
        return repairTicketRepository.findAll()
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public RepairTicketDto updateTicket(Long currentUserId, boolean isAdmin, Long ticketId, RepairTicketUpdateRequest req) {
        RepairTicket ticket = repairTicketRepository.findById(ticketId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "维修信息不存在"));

        if (!isAdmin) {
            if (ticket.getAssignedWorkerUserId() == null || !ticket.getAssignedWorkerUserId().equals(currentUserId)) {
                throw new BusinessException(ErrorCode.FORBIDDEN, "无法处理他人的工单");
            }
        }

        ticket.setStatus(req.getStatus());
        if (req.getRepairCost() != null) {
            ticket.setRepairCost(req.getRepairCost());
        }
        ticket.setHandlerNotes(req.getHandlerNotes());
        ticket.setUpdatedAt(Instant.now());

        RepairTicket saved = repairTicketRepository.save(ticket);

        if (req.getStatus() == RepairTicketStatus.DONE || req.getStatus() == RepairTicketStatus.CLOSED) {
            if (ticket.getParkingSpaceId() != null) {
                parkingSpaceRepository.findById(ticket.getParkingSpaceId()).ifPresent(space -> {
                    if (!"REPAIR".equalsIgnoreCase(space.getStatus())) {
                        return;
                    }
                    space.setStatus("AVAILABLE");
                    parkingSpaceRepository.save(space);
                });
            }
        }

        return toDto(saved);
    }

    private RepairTicketDto toDto(RepairTicket t) {
        RepairTicketDto dto = new RepairTicketDto();
        dto.setId(t.getId());
        dto.setReporterUserId(t.getReporterUserId());
        dto.setParkingLotId(t.getParkingLotId());
        dto.setParkingSpaceId(t.getParkingSpaceId());
        dto.setAssignedWorkerUserId(t.getAssignedWorkerUserId());
        dto.setDescription(t.getDescription());
        dto.setStatus(t.getStatus());
        dto.setRepairCost(t.getRepairCost());
        dto.setHandlerNotes(t.getHandlerNotes());
        dto.setCreatedAt(t.getCreatedAt());
        dto.setUpdatedAt(t.getUpdatedAt());
        return dto;
    }
}

