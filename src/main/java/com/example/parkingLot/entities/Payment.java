package com.example.parkingLot.entities;

import com.example.parkingLot.enums.PaymentMethod;
import com.example.parkingLot.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payments",
        indexes = {
                @Index(name = "idx_payment_status", columnList = "paymentStatus"),
                @Index(name = "idx_payment_method", columnList = "paymentMethod")
        })
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @NotNull
    private Double amount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    private LocalDateTime processedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;
}
