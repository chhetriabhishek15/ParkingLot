package com.example.parkingLot.entities;

import com.example.parkingLot.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicles",
indexes = @Index(name = "idx_vehicle_type", columnList = "vehicleType")
)
public class Vehicle {
    @Id
    @NotBlank
    private String vehicleNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    @OneToMany(mappedBy = "vehicle",cascade = {CascadeType.PERSIST, CascadeType.MERGE},fetch = FetchType.LAZY)
    private List<Ticket> ticketList;
}
