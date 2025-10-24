package com.example.parkingLot.service;

import com.example.parkingLot.dto.FloorSummaryDto;
import com.example.parkingLot.entity.ParkingFloor;
import com.example.parkingLot.entity.ParkingSpot;
import com.example.parkingLot.enums.ParkingFloorStatus;
import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.repository.ParkingFloorRepository;
import com.example.parkingLot.repository.ParkingSpotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingFloorServiceTest {
    @Mock ParkingFloorRepository floorRepository;
    @Mock ParkingSpotRepository spotRepository;
    @InjectMocks ParkingFloorService floorService;

    @Test
    void getAllFloors_shouldReturnSummaryList() {
        ParkingFloor floor = ParkingFloor.builder().floorId(1L).floorNumber(1).parkingFloorStatus(ParkingFloorStatus.AVAILABLE).build();
        when(floorRepository.findAll()).thenReturn(List.of(floor));
        when(spotRepository.findByFloor_FloorId(1L)).thenReturn(Collections.emptyList());
        List<FloorSummaryDto> result = floorService.getAllFloors();
        assertEquals(1, result.size());
        verify(floorRepository).findAll();
    }

    @Test
    void getFloorById_shouldReturnSummary_whenFloorExists() throws Exception {
        ParkingFloor floor = ParkingFloor.builder().floorId(2L).floorNumber(2).parkingFloorStatus(ParkingFloorStatus.AVAILABLE).build();
        when(floorRepository.findById(2L)).thenReturn(Optional.of(floor));
        when(spotRepository.findByFloor_FloorId(2L)).thenReturn(Collections.emptyList());
        FloorSummaryDto result = floorService.getFloorById(2L);
        assertEquals(2L, result.getFloorId());
        verify(floorRepository).findById(2L);
    }

    @Test
    void getFloorById_shouldThrow_whenNotFound() {
        when(floorRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> floorService.getFloorById(3L));
    }

    @Test
    void updateFloorStatus_shouldUpdateStatus_whenValid() throws Exception {
        ParkingFloor floor = ParkingFloor.builder().floorId(4L).floorNumber(4).parkingFloorStatus(ParkingFloorStatus.AVAILABLE).build();
        List<ParkingSpot> spots = new ArrayList<>();
        when(floorRepository.findById(4L)).thenReturn(Optional.of(floor));
        when(spotRepository.findByFloor_FloorId(4L)).thenReturn(spots);
        when(floorRepository.save(any())).thenReturn(floor);
        FloorSummaryDto result = floorService.updateFloorStatus(4L, ParkingFloorStatus.OUT_OF_SERVICE);
        assertEquals(4L, result.getFloorId());
        verify(floorRepository).findById(4L);
        verify(floorRepository).save(any());
        verify(spotRepository).saveAll(spots);
    }

    @Test
    void updateFloorStatus_shouldThrowConflict_whenOccupiedSpot() {
        ParkingFloor floor = ParkingFloor.builder().floorId(5L).floorNumber(5).parkingFloorStatus(ParkingFloorStatus.AVAILABLE).build();
        ParkingSpot occupiedSpot = ParkingSpot.builder().isOccupied(true).build();
        when(floorRepository.findById(5L)).thenReturn(Optional.of(floor));
        when(spotRepository.findByFloor_FloorId(5L)).thenReturn(List.of(occupiedSpot));
        assertThrows(ConflictException.class, () -> floorService.updateFloorStatus(5L, ParkingFloorStatus.OUT_OF_SERVICE));
    }
}

