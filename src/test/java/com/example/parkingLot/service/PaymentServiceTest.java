package com.example.parkingLot.service;

import com.example.parkingLot.dto.PaymentDto;
import com.example.parkingLot.entity.Payment;
import com.example.parkingLot.entity.Ticket;
import com.example.parkingLot.enums.PaymentMethod;
import com.example.parkingLot.enums.PaymentStatus;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.repository.PaymentRepository;
import com.example.parkingLot.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock PaymentRepository paymentRepository;
    @Mock TicketRepository ticketRepository;
    @InjectMocks PaymentService paymentService;

    @Test
    void createPayment_shouldSaveAndReturnPayment() {
        Ticket ticket = Ticket.builder().ticketId(1L).build();
        Payment payment = Payment.builder().ticket(ticket).amount(50.0).paymentMethod(PaymentMethod.CARD).paymentStatus(PaymentStatus.COMPLETED).build();
        when(paymentRepository.save(any())).thenReturn(payment);
        Payment result = paymentService.createPayment(ticket, 50.0, PaymentMethod.CARD);
        assertEquals(ticket, result.getTicket());
        assertEquals(50.0, result.getAmount());
        assertEquals(PaymentMethod.CARD, result.getPaymentMethod());
        assertEquals(PaymentStatus.COMPLETED, result.getPaymentStatus());
        verify(paymentRepository).save(any());
    }

    @Test
    void getByTicketId_shouldReturnPaymentDto_whenExists() throws Exception {
        Ticket ticket = Ticket.builder().ticketId(2L).build();
        Payment payment = Payment.builder().ticket(ticket).amount(30.0).paymentMethod(PaymentMethod.CASH).paymentStatus(PaymentStatus.COMPLETED).build();
        when(paymentRepository.findByTicket_TicketId(2L)).thenReturn(Optional.of(payment));
        PaymentDto result = paymentService.getByTicketId(2L);
        assertEquals(2L, result.getTicketId());
        assertEquals(30.0, result.getAmount());
        assertEquals(PaymentMethod.CASH, result.getMethod());
        verify(paymentRepository).findByTicket_TicketId(2L);
    }

    @Test
    void getByTicketId_shouldThrow_whenNotFound() {
        when(paymentRepository.findByTicket_TicketId(3L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> paymentService.getByTicketId(3L));
    }
}

