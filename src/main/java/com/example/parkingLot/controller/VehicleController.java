package com.example.parkingLot.controller;

import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.dto.CreateVehicleRequest;
import com.example.parkingLot.dto.VehicleDto;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicles")
public class VehicleController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);

    private final VehicleService vehicleService;

    @GetMapping()
    public ResponseEntity<List<VehicleDto>> listAll(){
        return ResponseEntity.ok(vehicleService.listAll());
    }

    @GetMapping("/{vehicleNumber}")
    public ResponseEntity<VehicleDto> get(@PathVariable String vehicleNumber) throws ResourceNotFoundException {
        return ResponseEntity.ok(vehicleService.getById(vehicleNumber));
    }

    @PostMapping()
    public ResponseEntity<VehicleDto> create(@Valid @RequestBody CreateVehicleRequest request) throws ConflictException {
        VehicleDto dto = vehicleService.create(request);
        return ResponseEntity.created(URI.create("/api/vehicles/"+dto.getVehicleNumber())).body(dto);
    }

    @DeleteMapping("/{vehicleNumber}")
    public ResponseEntity<Void> delete(@PathVariable String vehicleNumber) throws ResourceNotFoundException {
        vehicleService.delete(vehicleNumber);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<String> handleConflictException(ConflictException ex){
        logger.error("Conflict error: {}", ex.getMessage());
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        logger.error("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
