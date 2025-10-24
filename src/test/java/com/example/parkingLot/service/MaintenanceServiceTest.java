package com.example.parkingLot.service;

import com.example.parkingLot.dto.CreateMaintenanceRequest;
import com.example.parkingLot.dto.MaintenanceDto;
import com.example.parkingLot.entity.MaintenanceBoard;
import com.example.parkingLot.entity.ParkingFloor;
import com.example.parkingLot.entity.ParkingSpot;
import com.example.parkingLot.enums.MaintenanceType;
import com.example.parkingLot.enums.ParkingFloorStatus;
import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.repository.MaintenanceBoardRepository;
import com.example.parkingLot.repository.ParkingFloorRepository;
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
class MaintenanceServiceTest {
    @Mock MaintenanceBoardRepository maintenanceRepository;
    @Mock ParkingSpotRepository spotRepository;
    @Mock ParkingFloorRepository floorRepository;
    @Mock ParkingFloorService floorService;
    @InjectMocks MaintenanceService maintenanceService;

    @Test
    void startMaintenance_shouldStartFloorMaintenance_whenFloorExists() throws Exception {
        CreateMaintenanceRequest req = CreateMaintenanceRequest.builder()
                .type(MaintenanceType.FLOOR)
                .floorId(1L)
                .reason("Test")
                .performedBy("Admin")
                .build();
        ParkingFloor floor = ParkingFloor.builder().floorId(1L).build();
        when(floorRepository.findById(1L)).thenReturn(Optional.of(floor));
        MaintenanceBoard saved = MaintenanceBoard.builder().maintenanceId(10L).type(MaintenanceType.FLOOR).parkingFloor(floor).performedBy("Admin").reason("Test").build();
        when(maintenanceRepository.save(any())).thenReturn(saved);

        MaintenanceDto result = maintenanceService.startMaintenance(req);
        assertEquals(10L, result.getId());
        verify(floorRepository).findById(1L);
        verify(floorService).updateFloorStatus(1L, ParkingFloorStatus.OUT_OF_SERVICE);
        verify(maintenanceRepository).save(any());
    }

    @Test
    void startMaintenance_shouldThrow_whenFloorNotFound() {
        CreateMaintenanceRequest req = CreateMaintenanceRequest.builder()
                .type(MaintenanceType.FLOOR)
                .floorId(2L)
                .reason("Test")
                .performedBy("Admin")
                .build();
        when(floorRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> maintenanceService.startMaintenance(req));
    }

    @Test
    void startMaintenance_shouldThrow_whenSpotOccupied() {
        CreateMaintenanceRequest req = CreateMaintenanceRequest.builder()
                .type(MaintenanceType.SPOT)
                .spotId(3L)
                .reason("Test")
                .performedBy("Admin")
                .build();
        ParkingSpot spot = ParkingSpot.builder().spotId(3L).isOccupied(true).build();
        when(spotRepository.findById(3L)).thenReturn(Optional.of(spot));
        assertThrows(ConflictException.class, () -> maintenanceService.startMaintenance(req));
    }

    @Test
    void closeMaintenance_shouldCloseMaintenance_whenRecordExists() throws Exception {
        MaintenanceBoard record = MaintenanceBoard.builder().maintenanceId(5L).type(MaintenanceType.FLOOR).parkingFloor(ParkingFloor.builder().floorId(1L).build()).build();
        when(maintenanceRepository.findById(5L)).thenReturn(Optional.of(record));
        when(maintenanceRepository.save(any())).thenReturn(record);
        MaintenanceDto result = maintenanceService.closeMaintenance(5L);
        assertEquals(5L, result.getId());
        verify(maintenanceRepository).findById(5L);
        verify(maintenanceRepository).save(any());
    }

    @Test
    void closeMaintenance_shouldThrow_whenRecordNotFound() {
        when(maintenanceRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> maintenanceService.closeMaintenance(99L));
    }
}

