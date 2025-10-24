package com.example.parkingLot.controller;

import com.example.parkingLot.dto.FloorSummaryDto;
import com.example.parkingLot.enums.ParkingFloorStatus;
import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.service.ParkingFloorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/floors")
@RequiredArgsConstructor
public class ParkingFloorController {

    private static final Logger log = LoggerFactory.getLogger(ParkingFloorController.class);
    private final ParkingFloorService floorService;

    @GetMapping
    public ResponseEntity<List<FloorSummaryDto>> getAllFloors() {
        return ResponseEntity.ok(floorService.getAllFloors());
    }

    @GetMapping("/{floorId}")
    public ResponseEntity<FloorSummaryDto> getFloor(@PathVariable Long floorId) throws ResourceNotFoundException {
        return ResponseEntity.ok(floorService.getFloorById(floorId));
    }

    @PatchMapping("/{floorId}/status")
    public ResponseEntity<FloorSummaryDto> updateStatus(
            @PathVariable Long floorId,
            @RequestParam ParkingFloorStatus status)
            throws ConflictException, ResourceNotFoundException {
        log.info("Updating floor {} to status {}", floorId, status);
        return ResponseEntity.ok(floorService.updateFloorStatus(floorId, status));
    }
}

