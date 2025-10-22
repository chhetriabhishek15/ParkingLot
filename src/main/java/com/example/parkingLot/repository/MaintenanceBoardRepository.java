package com.example.parkingLot.repository;

import com.example.parkingLot.entities.MaintainanceBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceBoardRepository extends JpaRepository<MaintainanceBoard, Long> {
}
