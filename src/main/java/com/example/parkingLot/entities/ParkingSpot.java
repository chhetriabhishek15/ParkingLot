package com.example.parkingLot.entities;

import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.enums.ParkingSpotType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
                @Index(name = "idx_spot_type", columnList = "spotType"),
                @Index(name = "idx_spot_status", columnList = "parkingSpotStatus"),
                @Index(name = "idx_spot_occupied", columnList = "isOccupied")
        })
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long spotId;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String spotNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ParkingSpotType parkingSpotType;

    @Enumerated(EnumType.STRING)
    private ParkingSpotStatus parkingSpotStatus = ParkingSpotStatus.AVAILABLE;

    @Column(nullable = false)
    private boolean isOccupied = false;

    private Long version;

    private String currntVehicleNumber;

    private LocalDateTime lastOccupiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private ParkingFloor floor;

}
