package com.example.parkingLot.repository;

import com.example.parkingLot.entity.MaintenanceBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceBoardRepository extends JpaRepository<MaintenanceBoard, Long> {
}
