package com.example.parkingLot.entities;

import com.example.parkingLot.enums.GateType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "gates",
indexes = {
        @Index(name = "idx_gate_type",columnList = "gateType"),
        @Index(name = "idx_gate_name", columnList = "gateName")
})
public class Gate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gateId;

    @NotBlank
    @Column(nullable = false)
    private String gateName;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private GateType gateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;
}
