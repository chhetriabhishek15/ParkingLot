package com.example.parkingLot.service;

import com.example.parkingLot.dto.*;
import com.example.parkingLot.entity.*;
import com.example.parkingLot.enums.*;
import com.example.parkingLot.exception.*;
import com.example.parkingLot.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {
    private static final Logger logger = LoggerFactory.getLogger(ParkingSpotService.class);

    private final ParkingSpotRepository spotRepo;
    private final ParkingFloorRepository floorRepo;


    public List<ParkingSpotDto> listAll() {
        return spotRepo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    public List<ParkingSpotDto> listAvailableByType(ParkingSpotType type) {
        return spotRepo.findAvailableByTypeAndStatus(type, ParkingSpotStatus.AVAILABLE)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public ParkingSpotDto getById(Long id) throws ResourceNotFoundException {
        ParkingSpot s = spotRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spot not found: " + id));
        return toDto(s);
    }

    @Transactional
    public ParkingSpotDto create(@Valid CreateSpotRequest spotRequest) throws ResourceNotFoundException {

        ParkingFloor floor = null;
        if (spotRequest.getFloorId()!=null){
            floor = floorRepo.findById(spotRequest.getFloorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Floor not found: " + spotRequest.getFloorId()));
        }

        ParkingSpot spot = ParkingSpot.builder()
                .spotNumber(spotRequest.getSpotNumber())
                .parkingSpotType(spotRequest.getParkingSpotType())
                .parkingSpotStatus(ParkingSpotStatus.AVAILABLE)
                .isOccupied(false)
                .floor(floor)
                .build();

        ParkingSpot parkingSpot = spotRepo.save(spot);
        logger.info("Created parking spot {}", parkingSpot.getSpotNumber());

        return toDto(parkingSpot);
    }

    @Transactional
    public ParkingSpotDto updateStatus(Long spotId, @Valid UpdateSpotStatusRequest req) throws ResourceNotFoundException {
        ParkingSpot s = spotRepo.findById(spotId)
                .orElseThrow(() -> new ResourceNotFoundException("Spot not found: " + spotId));

        s.setParkingSpotStatus(req.getParkingSpotStatus());
        ParkingSpot spot = spotRepo.save(s);

        logger.info("Updated spot {} status to {}", s.getSpotNumber(), req.getParkingSpotStatus());

        return toDto(spot);
    }

    @Transactional
    public ParkingSpotDto occupySpotWithRetry(Long spotId, String vehicleNumber, int maxAttempts)
            throws ResourceNotFoundException, SpotAlreadyOccupiedException {
        int attempts = 0;
        while (true) {
            try {
                //This will update the version , optimistic lock set Version = 1 , which prevents concurrency as when save and flush its set to 2
                ParkingSpot s = spotRepo.findById(spotId).
                        orElseThrow(() -> new ResourceNotFoundException("Spot not found: " + spotId));
                if (s.isOccupied()){
                    throw new SpotAlreadyOccupiedException("Spot already occupied: " + spotId);
                }
                s.setOccupied(true);
                s.setParkingSpotStatus(ParkingSpotStatus.OCCUPIED);
                s.setCurrntVehicleNumber(vehicleNumber);

                // This forces to , version check
                ParkingSpot saved = spotRepo.saveAndFlush(s);

                return toDto(saved);
            }
            catch (OptimisticLockingFailureException ex){
                logger.warn("Optimistic lock failure on occupySpot (attempt {}): spotId={}", attempts, spotId);
                if (attempts >= maxAttempts) {
                    throw ex;
                }
            }
            catch (DataAccessException dae){
                logger.error("Database error occupying spot {}", spotId, dae);
                throw dae;
            }
        }
    }

    public ParkingSpotDto freeSpot(Long spotId) throws ResourceNotFoundException {
        ParkingSpot s = spotRepo.findById(spotId).
                orElseThrow(() -> new ResourceNotFoundException("Spot not found: " + spotId));
        s.setOccupied(false);
        s.setParkingSpotStatus(ParkingSpotStatus.AVAILABLE);
        s.setCurrntVehicleNumber(null);

        ParkingSpot saved = spotRepo.save(s);
        logger.info("Freed spot {}", s.getSpotNumber());
        return toDto(saved);
    }

    private ParkingSpotDto toDto(ParkingSpot s){
        return ParkingSpotDto.builder()
                .spotId(s.getSpotId())
                .spotNumber(s.getSpotNumber())
                .parkingSpotType(s.getParkingSpotType())
                .parkingSpotStatus(s.getParkingSpotStatus())
                .isOccupied(s.isOccupied())
                .floorId(s.getFloor() != null ? s.getFloor().getFloorId() : null)
                .currentVehicleNumber(s.getCurrntVehicleNumber())
                .build();
    }


}
