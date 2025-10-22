package com.example.parkingLot.repository;

import com.example.parkingLot.entities.DisplayBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisplayBoardRepository extends JpaRepository<DisplayBoard, Long> {
}
