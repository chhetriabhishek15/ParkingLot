package com.example.parkingLot.service;

import com.example.parkingLot.dto.FloorSummaryDto;
import com.example.parkingLot.dto.LotSummaryDto;
import com.example.parkingLot.enums.PaymentStatus;
import com.example.parkingLot.enums.TicketStatus;
import com.example.parkingLot.repository.MaintenanceBoardRepository;
import com.example.parkingLot.repository.PaymentRepository;
import com.example.parkingLot.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTest {
    @Mock ParkingFloorService floorService;
    @Mock TicketRepository ticketRepository;
    @Mock PaymentRepository paymentRepository;
    @Mock MaintenanceBoardRepository maintenanceRepository;
    @InjectMocks ParkingLotService parkingLotService;

    @Test
    void getParkingLotSummary_shouldReturnCorrectSummary() {
        FloorSummaryDto floor1 = FloorSummaryDto.builder().totalSpots(10).availableSpots(4).occupiedSpots(6).build();
        FloorSummaryDto floor2 = FloorSummaryDto.builder().totalSpots(8).availableSpots(2).occupiedSpots(6).build();
        List<FloorSummaryDto> floors = List.of(floor1, floor2);
        when(floorService.getAllFloors()).thenReturn(floors);
        when(ticketRepository.countByParkingTicketStatus(TicketStatus.ACTIVE)).thenReturn(5L);
        when(paymentRepository.sumAmountByPaymentStatus(PaymentStatus.COMPLETED)).thenReturn(100.0);
        when(maintenanceRepository.countByEndTimeIsNull()).thenReturn(2L);

        LotSummaryDto result = parkingLotService.getParkingLotSummary();
        assertEquals(18, result.getTotalSpots());
        assertEquals(6, result.getTotalAvailableSpots());
        assertEquals(12, result.getTotalOccupiedSpots());
        assertEquals(5, result.getActiveTickets());
        assertEquals(100.0, result.getTotalRevenueCollected());
        assertEquals(2, result.getMaintenanceInProgress());
        assertEquals(floors, result.getFloors());
    }

    @Test
    void getFloors_shouldReturnAllFloors() {
        List<FloorSummaryDto> floors = List.of(FloorSummaryDto.builder().floorId(1L).build());
        when(floorService.getAllFloors()).thenReturn(floors);
        List<FloorSummaryDto> result = parkingLotService.getFloors();
        assertEquals(floors, result);
        verify(floorService).getAllFloors();
    }
}

