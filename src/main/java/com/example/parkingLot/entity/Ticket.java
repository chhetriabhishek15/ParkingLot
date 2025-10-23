package com.example.parkingLot.entity;

import com.example.parkingLot.enums.GateType;
import com.example.parkingLot.enums.TicketStatus;
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
@Table(name = "tickets",
        indexes = {
                @Index(name = "idx_ticket_status", columnList = "parkingTicketStatus"),
                @Index(name = "idx_ticket_entry_time", columnList = "entryTime")
        })
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_number")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private ParkingSpot parkingSpot;

    @Enumerated(EnumType.STRING)
    private GateType entryGateType;

    @Enumerated(EnumType.STRING)
    private GateType exitGateType;

    @NotNull
    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    @Enumerated(EnumType.STRING)
    private TicketStatus parkingTicketStatus = TicketStatus.ACTIVE;

    private Double fee;

    @OneToOne(mappedBy = "ticket",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private Payment payment;
}
