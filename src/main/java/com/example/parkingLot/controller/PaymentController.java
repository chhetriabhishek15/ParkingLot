package com.example.parkingLot.controller;

import com.example.parkingLot.dto.PaymentDto;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
class PaymentController{
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<PaymentDto> getPaymentByTicket(@PathVariable Long ticketId)
        throws ResourceNotFoundException {
        log.info("Fetching payment for ticket {}", ticketId);
        return ResponseEntity.ok(paymentService.getByTicketId(ticketId));
    }
}