package com.example.parkingLot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "maintenance_records",
        indexes = @Index(name = "idx_maintenance_start", columnList = "startTime"))
public class MaintainanceBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long maintainanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spot_id")
    private ParkingSpot parkingSpot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private ParkingFloor parkingFloor;

    @NotBlank
    private String reason;

    @NotNull
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String performedBy;
}
