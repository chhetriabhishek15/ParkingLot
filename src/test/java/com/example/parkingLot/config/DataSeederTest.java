package com.example.parkingLot.config;

import com.example.parkingLot.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {
    @Mock
    ParkingLotRepository parkingLotRepository;
    @Mock
    ParkingFloorRepository parkingFloorRepository;
    @Mock
    ParkingSpotRepository parkingSpotRepository;
    @Mock
    GateRepository gateRepository;
    @Mock
    VehicleRepository vehicleRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    MaintenanceBoardRepository maintenanceRecordRepository;
    @Mock
    DisplayBoardRepository displayBoardRepository;
    @InjectMocks
    DataSeeder dataSeeder;

    @Test
    void run_shouldSkipSeeding_whenDataAlreadyPresent() throws Exception {
        when(parkingLotRepository.count()).thenReturn(1L);
        dataSeeder.run();
        verify(parkingLotRepository).count();
        verifyNoMoreInteractions(parkingFloorRepository, parkingSpotRepository, gateRepository, vehicleRepository, ticketRepository, paymentRepository, maintenanceRecordRepository, displayBoardRepository);
    }

}