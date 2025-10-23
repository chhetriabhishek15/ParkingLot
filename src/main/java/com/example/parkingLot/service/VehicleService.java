package com.example.parkingLot.service;

import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.dto.CreateVehicleRequest;
import com.example.parkingLot.dto.VehicleDto;
import com.example.parkingLot.entity.Vehicle;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private static final Logger log = LoggerFactory.getLogger(VehicleService.class);
    private final VehicleRepository vehicleRepository;

    public List<VehicleDto> listAll(){
        log.info("Returning vehicles ");
        return vehicleRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public VehicleDto getById(String vehicleNumber) throws ResourceNotFoundException {
        Vehicle v = vehicleRepository.findById(vehicleNumber)
                .orElseThrow(()->new ResourceNotFoundException("Vehicle not found: " + vehicleNumber));

        log.info("Vehicle returned successfully. {}", vehicleNumber);
        return toDto(v);
    }

    @Transactional
    public VehicleDto create(@Valid CreateVehicleRequest request) throws ConflictException {
        if (vehicleRepository.existsById(request.getVehicleNumber())){
            throw new ConflictException("Vehicle already exists: " + request.getVehicleNumber());
        }

        Vehicle v = Vehicle.builder()
                .vehicleNumber(request.getVehicleNumber())
                .vehicleType(request.getVehicleType())
                .build();

        Vehicle saved = vehicleRepository.save(v);
        log.info("Created vehicle: {}", saved.getVehicleNumber());
        return toDto(saved);
    }

    @Transactional
    public void delete(String vehicleNumber) throws ResourceNotFoundException {
        Vehicle v = vehicleRepository.findById(vehicleNumber)
                .orElseThrow(()->new ResourceNotFoundException("Vehicle not found: " + vehicleNumber));
        vehicleRepository.delete(v);
        log.info("Deleted vehicle {}", vehicleNumber);
    }

    private VehicleDto toDto(Vehicle v){
        return VehicleDto.builder()
                .vehicleNumber(v.getVehicleNumber())
                .vehicleType(v.getVehicleType())
                .build();
    }



}
