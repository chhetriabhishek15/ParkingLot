package com.example.parkingLot.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "display_boards",
        indexes = @Index(name = "idx_display_board_name", columnList = "boardName"))
public class DisplayBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dislpayBoardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private ParkingFloor floor;

    @NotBlank
    @Column(nullable = false)
    private String boardName;

    private String updateChannel;
}
