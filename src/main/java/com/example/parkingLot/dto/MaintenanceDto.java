package com.example.parkingLot.dto;

import com.example.parkingLot.enums.MaintenanceType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceDto {
    private Long id;
    private MaintenanceType type;
    private Long floorId;
    private Long spotId;
    private String reason;
    private String performedBy;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
