package com.example.parkingLot.controller;

import com.example.parkingLot.dto.*;
import com.example.parkingLot.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lot")
@RequiredArgsConstructor
public class ParkingLotController {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotController.class);
    private final ParkingLotService parkingLotService;

    @GetMapping("/summary")
    public ResponseEntity<LotSummaryDto> getParkingLotSummary() {
        log.info("Fetching parking lot summary");
        LotSummaryDto summary = parkingLotService.getParkingLotSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/floors")
    public ResponseEntity<List<FloorSummaryDto>> getFloors() {
        return ResponseEntity.ok(parkingLotService.getFloors());
    }
}