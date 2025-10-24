package com.example.parkingLot.controller;

import com.example.parkingLot.dto.CreateMaintenanceRequest;
import com.example.parkingLot.dto.MaintenanceDto;
import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.service.MaintenanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @PostMapping("/start")
    public ResponseEntity<MaintenanceDto> startMaintenance(@Valid @RequestBody CreateMaintenanceRequest req)
        throws ConflictException, ResourceNotFoundException {
        return ResponseEntity.ok(maintenanceService.startMaintenance(req));
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<MaintenanceDto> closeMaintenance(@PathVariable Long id)
        throws ConflictException, ResourceNotFoundException {
        return ResponseEntity.ok(maintenanceService.closeMaintenance(id));
    }

}
