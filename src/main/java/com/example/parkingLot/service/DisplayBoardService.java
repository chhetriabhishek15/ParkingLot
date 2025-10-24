package com.example.parkingLot.service;

import com.example.parkingLot.controller.DisplayBoardController;
import com.example.parkingLot.dto.FloorSummaryDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DisplayBoardService {

    private static final Logger log = LoggerFactory.getLogger(DisplayBoardController.class);
    private final ParkingFloorService floorService;

    public Map<String, Object> getDisplaySummary() {

        List<FloorSummaryDto> floors = floorService.getAllFloors();

        long totalSpots = floors.stream().mapToLong(FloorSummaryDto::getTotalSpots).sum();
        long totalAvailable = floors.stream().mapToLong(FloorSummaryDto::getAvailableSpots).sum();
        long totalOccupied = floors.stream().mapToLong(FloorSummaryDto::getOccupiedSpots).sum();

        Map<String, Object> response = new HashMap<>();

        response.put("totalSpots", totalSpots);
        response.put("availableSpots", totalAvailable);
        response.put("occupiedSpots", totalOccupied);
        response.put("floors", floors);

        log.info("Display summary requested: {} floors total", floors.size());

        return response;
    }

}
