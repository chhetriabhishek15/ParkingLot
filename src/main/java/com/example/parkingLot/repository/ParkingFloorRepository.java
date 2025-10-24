package com.example.parkingLot.repository;

import com.example.parkingLot.entity.ParkingFloor;
import com.example.parkingLot.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParkingFloorRepository extends JpaRepository<ParkingFloor, Long> {
}
