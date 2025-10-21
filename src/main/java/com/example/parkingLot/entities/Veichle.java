package com.example.parkingLot.entities;

import com.example.parkingLot.enums.VeichleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "veichles")
public class Veichle {
    @Id
    private String veichleNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VeichleType veichleType;
}
