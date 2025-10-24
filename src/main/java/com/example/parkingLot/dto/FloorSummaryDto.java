package com.example.parkingLot.dto;

import com.example.parkingLot.enums.ParkingFloorStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FloorSummaryDto {
    private Long floorId;
    private String floorNumber;
    private ParkingFloorStatus status;
    private long totalSpots;
    private long availableSpots;
    private long occupiedSpots;
}
