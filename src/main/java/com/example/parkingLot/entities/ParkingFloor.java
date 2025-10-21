package com.example.parkingLot.entities;

import com.example.parkingLot.enums.ParkingFloorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parking_floors")
public class ParkingFloor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long floortId;

    @Column(nullable = false)
    private int floorNumber;

    @Enumerated(EnumType.STRING)
    private ParkingFloorStatus parkingFloorStatus = ParkingFloorStatus.AVAILABLE;

    @OneToMany(mappedBy = "floor", cascade = {CascadeType.PERSIST,CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<ParkingSpot> parkingSpots;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;
}
