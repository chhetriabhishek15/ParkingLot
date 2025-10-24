package com.example.parkingLot.dto;

import com.example.parkingLot.enums.MaintenanceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMaintenanceRequest {
    @NotNull
    private MaintenanceType type;
    private Long floorId;
    private Long spotId;
    @NotBlank
    private String reason;
    @NotBlank
    private String performedBy;
}
