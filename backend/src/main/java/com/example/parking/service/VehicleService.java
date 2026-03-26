package com.example.parking.service;

import com.example.parking.common.BusinessException;
import com.example.parking.common.ErrorCode;
import com.example.parking.dto.VehicleCreateRequest;
import com.example.parking.dto.VehicleDto;
import com.example.parking.entity.Vehicle;
import com.example.parking.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public VehicleDto createVehicle(Long userId, VehicleCreateRequest req) {
        String plate = req.getPlateNumber().toUpperCase();
        if (vehicleRepository.findByUserIdAndPlateNumber(userId, plate).isPresent()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "该用户已存在相同车牌车辆");
        }
        Vehicle v = new Vehicle();
        v.setUserId(userId);
        v.setPlateNumber(plate);
        v.setBrand(req.getBrand());
        v.setColor(req.getColor());
        v.setVehicleType(req.getVehicleType());
        return toDto(vehicleRepository.save(v));
    }

    public List<VehicleDto> listVehicles(Long currentUserId, boolean isAdmin) {
        List<Vehicle> list = isAdmin ? vehicleRepository.findAll() : vehicleRepository.findByUserId(currentUserId);
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    private VehicleDto toDto(Vehicle v) {
        VehicleDto dto = new VehicleDto();
        dto.setId(v.getId());
        dto.setPlateNumber(v.getPlateNumber());
        dto.setBrand(v.getBrand());
        dto.setColor(v.getColor());
        dto.setVehicleType(v.getVehicleType());
        return dto;
    }
}

