package com.example.parkingLot.entities;

import com.example.parkingLot.enums.VeichleType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Veichle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long veichleNumberPlate;

    private VeichleType veichleType;

    public Veichle(long veichleNumberPlate, VeichleType veichleType) {
        this.veichleNumberPlate = veichleNumberPlate;
        this.veichleType = veichleType;
    }

    public long getVeichleNumberPlate() {
        return veichleNumberPlate;
    }

    public void setVeichleNumberPlate(long veichleNumberPlate) {
        this.veichleNumberPlate = veichleNumberPlate;
    }

    public VeichleType getVeichleType() {
        return veichleType;
    }

    public void setVeichleType(VeichleType veichleType) {
        this.veichleType = veichleType;
    }
}
