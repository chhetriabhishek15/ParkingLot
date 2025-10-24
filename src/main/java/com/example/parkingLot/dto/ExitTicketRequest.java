package com.example.parkingLot.dto;

import com.example.parkingLot.enums.PaymentMethod;
import com.example.parkingLot.enums.GateType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExitTicketRequest {
    // Accept either ticketId or vehicleNumber. One of them must be present in controller validation.
    private Long ticketId;

    private String vehicleNumber;

    @NotNull
    private PaymentMethod paymentMethod;

    private GateType exitGateType;
}
