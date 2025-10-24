package com.example.parkingLot.repository;

import com.example.parkingLot.entity.Payment;
import com.example.parkingLot.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTicket_TicketId(Long ticketId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentStatus = :status")
    Double sumAmountByPaymentStatus(@Param("status") PaymentStatus status);

}

