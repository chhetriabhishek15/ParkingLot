package com.example.parkingLot.repository;

import com.example.parkingLot.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GateRepository extends JpaRepository<Gate, Long> {
}
