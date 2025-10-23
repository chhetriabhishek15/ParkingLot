package com.example.parkingLot.repository;

import com.example.parkingLot.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot,Long>{
    ParkingSpot findBySpotNumber(String spotNumber);
}
