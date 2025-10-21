package com.example.parkingLot.entities;

import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.enums.ParkingSpotType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "parking_spots",
indexes = {
        @Index(name = "idx_spot_type",columnList = "parkingSpotType"),
        @Index(name = "idx_spot_occupied", columnList = "isOccupied")
})
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    @Column(nullable = false)
    private String spotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParkingSpotType parkingSpotType;

    @Enumerated(EnumType.STRING)
    private ParkingSpotStatus parkingSpotStatus = ParkingSpotStatus.AVAILABLE;

    @Column(nullable = false)
    private boolean isOccupied = false;

    @Transient
    private AtomicBoolean occupiedFlag = new AtomicBoolean(false);

    private String currntVeichleNumber;

    private LocalDateTime lastOccupiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private ParkingFloor floor;

}
