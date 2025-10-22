package com.example.parkingLot.entities;

import com.example.parkingLot.enums.ParkingFloorStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parking_floors",
indexes = @Index(name = "idx_floor_number", columnList = "floorNumber")
)
public class ParkingFloor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long floorId;

    @NotNull
    @Column(nullable = false)
    private int floorNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ParkingFloorStatus parkingFloorStatus = ParkingFloorStatus.AVAILABLE;

    @OneToMany(mappedBy = "floor",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<ParkingSpot> parkingSpots;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;

    @OneToMany(mappedBy = "floor",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<DisplayBoard> displayBoards;
}
