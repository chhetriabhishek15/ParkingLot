package com.example.parkingLot.service;

import com.example.parkingLot.dto.CreateTicketRequest;
import com.example.parkingLot.dto.TicketDto;
import com.example.parkingLot.entity.ParkingSpot;
import com.example.parkingLot.entity.Ticket;
import com.example.parkingLot.entity.Vehicle;
import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.enums.ParkingSpotType;
import com.example.parkingLot.enums.VehicleType;
import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.exception.TicketNotFoundException;
import com.example.parkingLot.repository.TicketRepository;
import com.example.parkingLot.repository.VehicleRepository;
import com.example.parkingLot.repository.ParkingSpotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {
    @Mock TicketRepository ticketRepository;
    @Mock VehicleRepository vehicleRepository;
    @Mock ParkingSpotRepository spotRepository;
    @Mock ParkingSpotService parkingSpotService;
    @Mock PaymentService paymentService;
    @InjectMocks TicketService ticketService;

    @Test
    void get_shouldReturnTicketDto_whenTicketExists() throws Exception {
        Ticket ticket = Ticket.builder().ticketId(1L).build();
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        TicketDto result = ticketService.get(1L);
        assertEquals(1L, result.getTicketId());
        verify(ticketRepository).findById(1L);
    }

    @Test
    void get_shouldThrow_whenTicketNotFound() {
        when(ticketRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(TicketNotFoundException.class, () -> ticketService.get(2L));
    }

    @Test
    void createEntry_shouldCreateTicket_whenVehicleExists() throws Exception {
        CreateTicketRequest req = CreateTicketRequest.builder()
            .vehicleNumber("ABC123")
            .vehicleType(VehicleType.CAR)
            .requestedSpotType(ParkingSpotType.COMPACT)
            .build();
        Vehicle vehicle = Vehicle.builder().vehicleNumber("ABC123").vehicleType(VehicleType.CAR).build();
        when(vehicleRepository.findById("ABC123")).thenReturn(Optional.of(vehicle));
        ParkingSpot candidate = ParkingSpot.builder().spotId(10L).parkingSpotType(ParkingSpotType.COMPACT).build();
        when(spotRepository.findFirstByParkingSpotTypeAndIsOccupiedFalseAndParkingSpotStatus(
            eq(ParkingSpotType.COMPACT), eq(ParkingSpotStatus.AVAILABLE)))
            .thenReturn(Optional.of(candidate));
        when(parkingSpotService.occupySpotEntityWithRetry(10L, "ABC123", 3)).thenReturn(candidate);
        Ticket ticket = Ticket.builder().ticketId(3L).vehicle(vehicle).parkingSpot(candidate).build();
        when(ticketRepository.save(any())).thenReturn(ticket);
        TicketDto result = ticketService.createEntry(req);
        assertEquals(3L, result.getTicketId());
        verify(vehicleRepository).findById("ABC123");
        verify(spotRepository).findFirstByParkingSpotTypeAndIsOccupiedFalseAndParkingSpotStatus(
            eq(ParkingSpotType.COMPACT),eq(ParkingSpotStatus.AVAILABLE));
        verify(parkingSpotService).occupySpotEntityWithRetry(10L, "ABC123", 3);
        verify(ticketRepository).save(any());
    }

    @Test
    void createEntry_shouldRegisterNewVehicle_whenNotExists() throws Exception {
        CreateTicketRequest req = CreateTicketRequest.builder()
            .vehicleNumber("NEW999")
            .vehicleType(VehicleType.MOTORCYCLE)
            .requestedSpotType(ParkingSpotType.MOTORBIKE)
            .build();
        when(vehicleRepository.findById("NEW999")).thenReturn(Optional.empty());
        Vehicle newVehicle = Vehicle.builder().vehicleNumber("NEW999").vehicleType(VehicleType.MOTORCYCLE).build();
        when(vehicleRepository.save(any())).thenReturn(newVehicle);
        ParkingSpot candidate = ParkingSpot.builder().spotId(20L).parkingSpotType(ParkingSpotType.MOTORBIKE).build();
        when(spotRepository.findFirstByParkingSpotTypeAndIsOccupiedFalseAndParkingSpotStatus(
            eq(ParkingSpotType.MOTORBIKE), eq(com.example.parkingLot.enums.ParkingSpotStatus.AVAILABLE)))
            .thenReturn(Optional.of(candidate));
        when(parkingSpotService.occupySpotEntityWithRetry(20L, "NEW999", 3)).thenReturn(candidate);
        Ticket ticket = Ticket.builder().ticketId(4L).vehicle(newVehicle).parkingSpot(candidate).build();
        when(ticketRepository.save(any())).thenReturn(ticket);
        TicketDto result = ticketService.createEntry(req);
        assertEquals(4L, result.getTicketId());
        verify(vehicleRepository).findById("NEW999");
        verify(vehicleRepository).save(any());
        verify(spotRepository).findFirstByParkingSpotTypeAndIsOccupiedFalseAndParkingSpotStatus(
            eq(ParkingSpotType.MOTORBIKE), eq(com.example.parkingLot.enums.ParkingSpotStatus.AVAILABLE));
        verify(parkingSpotService).occupySpotEntityWithRetry(20L, "NEW999", 3);
        verify(ticketRepository).save(any());
    }
}
