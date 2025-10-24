package com.example.parkingLot.service;

import com.example.parkingLot.dto.CreateSpotRequest;
import com.example.parkingLot.dto.ParkingSpotDto;
import com.example.parkingLot.dto.UpdateSpotStatusRequest;
import com.example.parkingLot.entity.ParkingFloor;
import com.example.parkingLot.entity.ParkingSpot;
import com.example.parkingLot.enums.ParkingSpotStatus;
import com.example.parkingLot.enums.ParkingSpotType;
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
class ParkingSpotServiceTest {
    @Mock ParkingSpotRepository spotRepo;
    @Mock ParkingFloorRepository floorRepo;
    @InjectMocks ParkingSpotService spotService;

    @Test
    void listAll_shouldReturnAllSpots() {
        ParkingSpot spot = ParkingSpot.builder().spotId(1L).build();
        when(spotRepo.findAll()).thenReturn(List.of(spot));
        List<ParkingSpotDto> result = spotService.listAll();
        assertEquals(1, result.size());
        verify(spotRepo).findAll();
    }

    @Test
    void listAvailableByType_shouldReturnAvailableSpots() {
        ParkingSpot spot = ParkingSpot.builder().spotId(2L).parkingSpotType(ParkingSpotType.COMPACT).parkingSpotStatus(ParkingSpotStatus.AVAILABLE).build();
        when(spotRepo.findAvailableByTypeAndStatus(ParkingSpotType.COMPACT, ParkingSpotStatus.AVAILABLE)).thenReturn(List.of(spot));
        List<ParkingSpotDto> result = spotService.listAvailableByType(ParkingSpotType.COMPACT);
        assertEquals(1, result.size());
        verify(spotRepo).findAvailableByTypeAndStatus(ParkingSpotType.COMPACT, ParkingSpotStatus.AVAILABLE);
    }

    @Test
    void getById_shouldReturnSpot_whenExists() throws Exception {
        ParkingSpot spot = ParkingSpot.builder().spotId(3L).build();
        when(spotRepo.findById(3L)).thenReturn(Optional.of(spot));
        ParkingSpotDto result = spotService.getById(3L);
        assertEquals(3L, result.getSpotId());
        verify(spotRepo).findById(3L);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(spotRepo.findById(4L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> spotService.getById(4L));
    }

    @Test
    void create_shouldCreateSpot_whenFloorExists() throws Exception {
        CreateSpotRequest req = CreateSpotRequest.builder().spotNumber("A1").parkingSpotType(ParkingSpotType.COMPACT).floorId(5L).build();
        ParkingFloor floor = ParkingFloor.builder().floorId(5L).build();
        when(floorRepo.findById(5L)).thenReturn(Optional.of(floor));
        ParkingSpot spot = ParkingSpot.builder().spotId(6L).spotNumber("A1").floor(floor).build();
        when(spotRepo.save(any())).thenReturn(spot);
        ParkingSpotDto result = spotService.create(req);
        assertEquals("A1", result.getSpotNumber());
        verify(floorRepo).findById(5L);
        verify(spotRepo).save(any());
    }

    @Test
    void create_shouldThrow_whenFloorNotFound() {
        CreateSpotRequest req = CreateSpotRequest.builder().spotNumber("A2").parkingSpotType(ParkingSpotType.COMPACT).floorId(7L).build();
        when(floorRepo.findById(7L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> spotService.create(req));
    }

    @Test
    void updateStatus_shouldUpdateSpotStatus_whenSpotExists() throws Exception {
        ParkingSpot spot = ParkingSpot.builder().spotId(8L).parkingSpotStatus(ParkingSpotStatus.AVAILABLE).build();
        UpdateSpotStatusRequest req = UpdateSpotStatusRequest.builder().parkingSpotStatus(ParkingSpotStatus.OUT_OF_SERVICE).build();
        when(spotRepo.findById(8L)).thenReturn(Optional.of(spot));
        when(spotRepo.save(any())).thenReturn(spot);
        ParkingSpotDto result = spotService.updateStatus(8L, req);
        assertEquals(ParkingSpotStatus.OUT_OF_SERVICE, result.getParkingSpotStatus());
        verify(spotRepo).findById(8L);
        verify(spotRepo).save(any());
    }

    @Test
    void updateStatus_shouldThrow_whenSpotNotFound() {
        UpdateSpotStatusRequest req = UpdateSpotStatusRequest.builder().parkingSpotStatus(ParkingSpotStatus.OUT_OF_SERVICE).build();
        when(spotRepo.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> spotService.updateStatus(9L, req));
    }
}
