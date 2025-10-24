package com.example.parkingLot.service;

import com.example.parkingLot.dto.FloorSummaryDto;
import com.example.parkingLot.entity.ParkingFloor;
import com.example.parkingLot.entity.ParkingSpot;
import com.example.parkingLot.enums.ParkingFloorStatus;
import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.repository.ParkingFloorRepository;
import com.example.parkingLot.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParkingFloorService {

    private static final Logger log = LoggerFactory.getLogger(ParkingFloorService.class);

    private final ParkingFloorRepository floorRepository;
    private final ParkingSpotRepository spotRepository;

    public List<FloorSummaryDto> getAllFloors() {
        return floorRepository.findAll()
                .stream()
                .map(this::buildSummary)
                .collect(Collectors.toList());
    }

    public FloorSummaryDto getFloorById(Long floorId) throws ResourceNotFoundException {
        ParkingFloor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found: " + floorId));
        return buildSummary(floor);
    }

    public FloorSummaryDto updateFloorStatus(Long floorId, ParkingFloorStatus status)
            throws ResourceNotFoundException, ConflictException {
        ParkingFloor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found: " + floorId));

        if (floor.getParkingFloorStatus() == status) {
            log.info("No status change for floor {}", floor.getFloorNumber());
            return buildSummary(floor);
        }

        List<ParkingSpot> spots = spotRepository.findByFloor_FloorId(floorId);

        if (status == ParkingFloorStatus.OUT_OF_SERVICE){
            boolean hasOccupied = spots.stream().anyMatch(ParkingSpot::isOccupied);
            if (hasOccupied){
                throw new ConflictException("Cannot set floor OUT_OF_SERVICE; some spots are occupied.");
            }
            else {
                spots.forEach(spot -> spot.setParkingSpotStatus(ParkingSpotStatus.OUT_OF_SERVICE));
            }
        } else if (status == ParkingFloorStatus.AVAILABLE) {
            spots.stream()
                    .filter(spot -> spot.getParkingSpotStatus()==ParkingSpotStatus.OUT_OF_SERVICE &&
                            !spot.isOccupied())
                    .forEach(spot -> spot.setParkingSpotStatus(ParkingSpotStatus.AVAILABLE));
        }

        floor.setParkingFloorStatus(status);
        floorRepository.save(floor);
        spotRepository.saveAll(spots);

        log.info("Updated floor {} status to {}", floor.getFloorNumber(), status);
        return buildSummary(floor);
    }

    private FloorSummaryDto buildSummary(ParkingFloor floor) {
        List<ParkingSpot> parkingSpots = spotRepository.findByFloor_FloorId(floor.getFloorId());
        long total = parkingSpots.size();
        long available = parkingSpots.stream()
                .filter(s->s.getParkingSpotStatus()==ParkingSpotStatus.AVAILABLE && !s.isOccupied())
                .count();
        long occupied = parkingSpots.stream()
                .filter(ParkingSpot::isOccupied)
                .count();

        return FloorSummaryDto.builder()
                .floorId(floor.getFloorId())
                .floorNumber(String.valueOf(floor.getFloorNumber()))
                .status(floor.getParkingFloorStatus())
                .totalSpots(total)
                .availableSpots(available)
                .occupiedSpots(occupied)
                .build();

    }
}
