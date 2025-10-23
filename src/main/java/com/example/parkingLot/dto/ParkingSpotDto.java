package com.example.parkingLot.dto;

import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.enums.ParkingSpotType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpotDto {
    private Long spotId;
    private String spotNumber;
    private ParkingSpotType parkingSpotType;
    private ParkingSpotStatus parkingSpotStatus;
    private boolean isOccupied;
    private Long floorId;
    private String currentVehicleNumber;
}
