package com.example.parkingLot.dto;

import com.example.parkingLot.enums.PaymentMethod;
import com.example.parkingLot.enums.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    private Long id;
    private Long ticketId;
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime processedAt;
}
