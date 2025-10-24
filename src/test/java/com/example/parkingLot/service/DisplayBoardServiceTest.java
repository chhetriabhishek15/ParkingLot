package com.example.parkingLot.service;

import com.example.parkingLot.dto.FloorSummaryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisplayBoardServiceTest {
    @Mock
    private ParkingFloorService floorService;
    @InjectMocks
    private DisplayBoardService displayBoardService;

    @Test
    void getDisplaySummary_shouldReturnCorrectSummary_whenFloorsExist() {
        // Arrange
        FloorSummaryDto floor1 = FloorSummaryDto.builder()
                .floorId(1L)
                .totalSpots(10)
                .availableSpots(4)
                .occupiedSpots(6)
                .build();
        FloorSummaryDto floor2 = FloorSummaryDto.builder()
                .floorId(2L)
                .totalSpots(8)
                .availableSpots(2)
                .occupiedSpots(6)
                .build();
        List<FloorSummaryDto> floors = Arrays.asList(floor1, floor2);
        when(floorService.getAllFloors()).thenReturn(floors);

        // Act
        Map<String, Object> summary = displayBoardService.getDisplaySummary();

        // Assert
        assertEquals(18L, summary.get("totalSpots"));
        assertEquals(6L, summary.get("availableSpots"));
        assertEquals(12L, summary.get("occupiedSpots"));
        assertEquals(floors, summary.get("floors"));
        verify(floorService).getAllFloors();
    }

    @Test
    void getDisplaySummary_shouldReturnZeroes_whenNoFloorsExist() {
        // Arrange
        when(floorService.getAllFloors()).thenReturn(Collections.emptyList());

        // Act
        Map<String, Object> summary = displayBoardService.getDisplaySummary();

        // Assert
        assertEquals(0L, summary.get("totalSpots"));
        assertEquals(0L, summary.get("availableSpots"));
        assertEquals(0L, summary.get("occupiedSpots"));
        assertEquals(Collections.emptyList(), summary.get("floors"));
        verify(floorService).getAllFloors();
    }
}

