package com.example.parkingLot.dto;

import com.example.parkingLot.enums.ParkingSpotType;
import com.example.parkingLot.enums.GateType;
import com.example.parkingLot.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTicketRequest {
    @NotBlank
    private String vehicleNumber;

    @NotNull
    private VehicleType vehicleType;

    @NotNull
    private ParkingSpotType requestedSpotType;

    private GateType entryGateType;
}
