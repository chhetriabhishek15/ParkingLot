package com.example.parkingLot.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parking_lots")
public class ParkingLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotId;

    @Column(nullable = false)
    private String lotName;

    @OneToMany(mappedBy = "parkingLot" , cascade = {CascadeType.MERGE,CascadeType.PERSIST},fetch = FetchType.LAZY)
    private List<ParkingFloor> parkingFloors;

}
