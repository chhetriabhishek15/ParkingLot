package com.example.parkingLot.service;

import com.example.parkingLot.dto.PaymentDto;
import com.example.parkingLot.entity.Payment;
import com.example.parkingLot.entity.Ticket;
import com.example.parkingLot.enums.PaymentStatus;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.repository.PaymentRepository;
import com.example.parkingLot.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public Payment createPayment(Ticket ticket, double fee, com.example.parkingLot.enums.PaymentMethod method) {
        Payment payment = Payment.builder()
                .ticket(ticket)
                .amount(fee)
                .paymentMethod(method)
                .paymentStatus(PaymentStatus.COMPLETED)
                .processedAt(java.time.LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);
        log.info("Payment recorded for ticket {} amount {}", ticket.getTicketId(), fee);
        return saved;
    }

    public PaymentDto getByTicketId(Long ticketId) throws ResourceNotFoundException {
        Payment payment = paymentRepository.findByTicket_TicketId(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ticket: " + ticketId));
        return toDto(payment);
    }

    private PaymentDto toDto(Payment p){
        return PaymentDto.builder()
                .id(p.getTicketId())
                .ticketId(p.getTicket() != null ? p.getTicket().getTicketId() : null)
                .amount(p.getAmount())
                .method(p.getPaymentMethod())
                .status(p.getPaymentStatus())
                .processedAt(p.getProcessedAt())
                .build();
    }
}
