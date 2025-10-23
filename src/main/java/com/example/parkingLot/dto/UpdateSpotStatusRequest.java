package com.example.parkingLot.dto;

import com.example.parkingLot.enums.ParkingSpotStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSpotStatusRequest {
    @NotNull
    private ParkingSpotStatus parkingSpotStatus;
}
