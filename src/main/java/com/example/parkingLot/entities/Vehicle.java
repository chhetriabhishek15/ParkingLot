package com.example.parkingLot.entities;

import com.example.parkingLot.enums.VeichleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicles",
indexes = @Index(name = "idx_vehicle_type", columnList = "vehicleType")
)
public class Veichle {
    @Id
    @NotBlank
    private String veichleNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VeichleType veichleType;

    @OneToMany(mappedBy = "veichle",cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    private List<Ticket> ticketList;
}
