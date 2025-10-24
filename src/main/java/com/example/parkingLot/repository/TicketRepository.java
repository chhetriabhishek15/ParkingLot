package com.example.parkingLot.repository;

import com.example.parkingLot.entity.Ticket;
import com.example.parkingLot.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketIdAndParkingTicketStatus(Long id , TicketStatus status);

    Optional<Ticket> findByVehicle_VehicleNumberAndParkingTicketStatus(String vehicleNumber, TicketStatus status);

    long countByParkingTicketStatus(TicketStatus status);

}
