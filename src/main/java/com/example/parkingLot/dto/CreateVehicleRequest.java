package com.example.parkingLot.dto;

import com.example.parkingLot.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVehicleRequest {

    @NotBlank(message = "vehicleNumber is required")
    private String vehicleNumber;

    @NotNull(message = "vehicleType is required")
    private VehicleType vehicleType;
}
