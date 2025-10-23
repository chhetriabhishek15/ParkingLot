package com.example.parkingLot.dto;

import com.example.parkingLot.enums.ParkingSpotType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSpotRequest {
    @NotBlank
    private String spotNumber;

    @NotNull
    private ParkingSpotType parkingSpotType;

    private Long floorId;
}
