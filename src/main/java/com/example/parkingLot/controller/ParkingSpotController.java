package com.example.parkingLot.controller;

import com.example.parkingLot.dto.CreateSpotRequest;
import com.example.parkingLot.dto.ParkingSpotDto;
import com.example.parkingLot.dto.UpdateSpotStatusRequest;
import com.example.parkingLot.enums.ParkingSpotType;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.exception.SpotAlreadyOccupiedException;
import com.example.parkingLot.service.ParkingSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class ParkingSpotController {

    private final ParkingSpotService spotService;

    @GetMapping()
    public ResponseEntity<List<ParkingSpotDto>> listAll(){
        return ResponseEntity.ok(spotService.listAll());
    }

    @GetMapping("/{spotId}")
    public ResponseEntity<ParkingSpotDto> get(@PathVariable Long spotId) throws ResourceNotFoundException {
        return ResponseEntity.ok(spotService.getById(spotId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<ParkingSpotDto>> listAvailable(@RequestParam ParkingSpotType type) {
        return ResponseEntity.ok(spotService.listAvailableByType(type));
    }

    @PostMapping()
    public ResponseEntity<ParkingSpotDto> create(@Valid @RequestBody CreateSpotRequest spotRequest)
        throws ResourceNotFoundException {
        ParkingSpotDto spotDto = spotService.create(spotRequest);
        return ResponseEntity.created(URI.create("/api/spots"+spotDto.getSpotId())).body(spotDto);
    }

    @PatchMapping("/{spotId}/status")
    public ResponseEntity<ParkingSpotDto> updateStatus(@PathVariable Long spotId ,
        @Valid @RequestBody UpdateSpotStatusRequest req) throws ResourceNotFoundException {
        return ResponseEntity.ok(spotService.updateStatus(spotId,req));
    }

    @PostMapping("/{spotId}/occupy")
    public ResponseEntity<ParkingSpotDto> occupy(@PathVariable Long spotId,
        @RequestParam String vehicleNumber) throws ResourceNotFoundException, SpotAlreadyOccupiedException {
        ParkingSpotDto dto = spotService.occupySpotWithRetry(spotId, vehicleNumber, 3);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{spotId}/free")
    public ResponseEntity<ParkingSpotDto> freeSpot(@PathVariable Long spotId) throws ResourceNotFoundException {
        return ResponseEntity.ok(spotService.freeSpot(spotId));
    }

}
