package com.example.parkingLot.dto;

import com.example.parkingLot.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDto {
    private String vehicleNumber;
    private VehicleType vehicleType;
}
