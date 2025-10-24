package com.example.parkingLot.service;

import com.example.parkingLot.dto.CreateMaintenanceRequest;
import com.example.parkingLot.dto.MaintenanceDto;
import com.example.parkingLot.entity.MaintenanceBoard;
import com.example.parkingLot.entity.ParkingFloor;
import com.example.parkingLot.entity.ParkingSpot;
import com.example.parkingLot.enums.MaintenanceType;
import com.example.parkingLot.enums.ParkingFloorStatus;
import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.repository.MaintenanceBoardRepository;
import com.example.parkingLot.repository.ParkingFloorRepository;
import com.example.parkingLot.repository.ParkingSpotRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MaintenanceService {
    private static final Logger log = LoggerFactory.getLogger(MaintenanceService.class);

    private final MaintenanceBoardRepository maintenanceRepository;
    private final ParkingSpotRepository spotRepository;
    private final ParkingFloorRepository floorRepository;
    private final ParkingFloorService floorService;

    @Transactional
    public MaintenanceDto startMaintenance(@Valid CreateMaintenanceRequest req) throws ResourceNotFoundException, ConflictException {
        MaintenanceBoard maintenance = new MaintenanceBoard();
        maintenance.setType(req.getType());
        maintenance.setReason(req.getReason());
        maintenance.setPerformedBy(req.getPerformedBy());
        maintenance.setStartTime(LocalDateTime.now());

        if (req.getType()==MaintenanceType.FLOOR){
            ParkingFloor floor = floorRepository.findById(req.getFloorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Floor not found: " + req.getFloorId()));
            floorService.updateFloorStatus(floor.getFloorId(),ParkingFloorStatus.OUT_OF_SERVICE);
            maintenance.setParkingFloor(floor);
        } else if (req.getType() == MaintenanceType.SPOT) {
            ParkingSpot spot = spotRepository.findById(req.getSpotId())
                    .orElseThrow(() -> new ResourceNotFoundException("Spot not found: " + req.getSpotId()));
            if (spot.isOccupied()){
                throw new ConflictException("Cannot perform maintenance on an occupied spot: " + req.getSpotId());
            }
            spot.setParkingSpotStatus(ParkingSpotStatus.OUT_OF_SERVICE);
            spotRepository.save(spot);
            maintenance.setParkingSpot(spot);
            maintenance.setParkingFloor(spot.getFloor());
        }

        MaintenanceBoard saved = maintenanceRepository.save(maintenance);
        log.info("Maintenance started: {} type={} by {}", saved.getMaintenanceId(), req.getType(), req.getPerformedBy());
        return toDto(saved);
    }

    @Transactional
    public MaintenanceDto closeMaintenance(Long id) throws ResourceNotFoundException, ConflictException {
        MaintenanceBoard record = maintenanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance record not found: " + id));

        record.setEndTime(LocalDateTime.now());

        if (record.getType() == MaintenanceType.FLOOR && record.getParkingFloor() != null) {
            floorService.updateFloorStatus(record.getParkingFloor().getFloorId(), ParkingFloorStatus.AVAILABLE);
        } else if (record.getType() == MaintenanceType.SPOT && record.getParkingSpot() != null) {
            record.getParkingSpot().setParkingSpotStatus(ParkingSpotStatus.AVAILABLE);
            spotRepository.save(record.getParkingSpot());
        }

        MaintenanceBoard updated = maintenanceRepository.save(record);
        log.info("Maintenance closed: {}", id);
        return toDto(updated);
    }

    private MaintenanceDto toDto(MaintenanceBoard m) {
        return MaintenanceDto.builder()
                .id(m.getMaintenanceId())
                .type(m.getType())
                .floorId(m.getParkingFloor() != null ? m.getParkingFloor().getFloorId() : null)
                .spotId(m.getParkingSpot() != null ? m.getParkingSpot().getSpotId() : null)
                .reason(m.getReason())
                .performedBy(m.getPerformedBy())
                .startTime(m.getStartTime())
                .endTime(m.getEndTime())
                .build();
    }
}
