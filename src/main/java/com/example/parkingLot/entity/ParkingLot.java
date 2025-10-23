package com.example.parkingLot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parking_lots",
        indexes = @Index(name = "idx_lot_name", columnList = "lotName"))
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;

    @Column(nullable = false)
    private String lotName;

    private String location;

    @OneToMany(mappedBy = "parkingLot" ,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    private List<ParkingFloor> parkingFloors;

    @OneToMany(mappedBy = "parkingLot",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    private List<Gate> gates;
}
