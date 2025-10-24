package com.example.parkingLot.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LotSummaryDto {
    private String lotName;
    private long totalSpots;
    private long totalAvailableSpots;
    private long totalOccupiedSpots;
    private long activeTickets;
    private double totalRevenueCollected;
    private long maintenanceInProgress;
    private List<FloorSummaryDto> floors;
}