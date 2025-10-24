package com.example.parkingLot.repository;

import com.example.parkingLot.entity.ParkingSpot;
import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.enums.ParkingSpotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot,Long>{
    ParkingSpot findBySpotNumber(String spotNumber);

    @Query("SELECT s FROM ParkingSpot s WHERE s.parkingSpotType = :type " +
            "AND s.isOccupied = false AND s.parkingSpotStatus = :status")
    List<ParkingSpot> findAvailableByTypeAndStatus(@Param("type") ParkingSpotType type,
                                                   @Param("status") ParkingSpotStatus status);

    Optional<ParkingSpot> findFirstByParkingSpotTypeAndIsOccupiedFalseAndParkingSpotStatus(ParkingSpotType type,
                                                                                           ParkingSpotStatus status);

    List<ParkingSpot> findByFloor_FloorId(Long floorId);
}

