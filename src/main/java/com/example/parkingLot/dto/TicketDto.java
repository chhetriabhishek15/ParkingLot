package com.example.parkingLot.dto;

import com.example.parkingLot.enums.GateType;
import com.example.parkingLot.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketDto {
    private Long ticketId;
    private String vehicleNumber;
    private Long spotId;
    private String spotNumber;
    private GateType entryGateType;
    private GateType exitGateType;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private TicketStatus status;
    private Double fee;
}
