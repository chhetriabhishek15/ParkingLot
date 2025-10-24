package com.example.parkingLot.service;

import com.example.parkingLot.dto.CreateVehicleRequest;
import com.example.parkingLot.dto.VehicleDto;
import com.example.parkingLot.entity.Vehicle;
import com.example.parkingLot.enums.VehicleType;
import com.example.parkingLot.exception.ConflictException;
import com.example.parkingLot.exception.ResourceNotFoundException;
import com.example.parkingLot.repository.VehicleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;


    private Vehicle buildVehicle(VehicleType type) {
        return Vehicle.builder()
                .vehicleNumber("WB34AB1234")
                .vehicleType(type)
                .build();
    }

    private CreateVehicleRequest buildCreateVehicleRequest() {
        return CreateVehicleRequest.builder()
                .vehicleNumber("WB34AB1234")
                .vehicleType(VehicleType.CAR)
                .build();
    }


    @Test
    void listAll_shouldReturnVehicleDtos_whenVehiclesExist() {
        // Arrange
        Vehicle vehicle = buildVehicle(VehicleType.CAR);
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicle));

        // Act
        List<VehicleDto> result = vehicleService.listAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("WB34AB1234", result.getFirst().getVehicleNumber());
        assertEquals(VehicleType.CAR, result.getFirst().getVehicleType());
        verify(vehicleRepository).findAll();
    }

    @Test
    void listAll_shouldReturnEmptyList_whenNoVehiclesExist() {
        // Arrange
        when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<VehicleDto> result = vehicleService.listAll();

        // Assert
        assertTrue(result.isEmpty());
        verify(vehicleRepository).findAll();
    }

    @Test
    void getById_shouldReturnVehicleDto_whenVehicleExists() throws Exception {
        // Arrange
        Vehicle vehicle = buildVehicle(VehicleType.MOTORCYCLE);
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(vehicle));

        // Act
        VehicleDto result = vehicleService.getById("WB34AB1234");

        // Assert
        assertEquals("WB34AB1234", result.getVehicleNumber());
        assertEquals(VehicleType.MOTORCYCLE, result.getVehicleType());
        verify(vehicleRepository).findById(eq("WB34AB1234"));
    }

    @Test
    void getById_shouldThrowResourceNotFoundException_whenVehicleDoesNotExist() {
        // Arrange
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getById("WB34AB1234"));
        verify(vehicleRepository).findById(eq("WB34AB1234"));
    }

    @Test
    void create_shouldSaveAndReturnVehicleDto_whenVehicleDoesNotExist() throws Exception {
        // Arrange
        CreateVehicleRequest request = buildCreateVehicleRequest();
        when(vehicleRepository.existsById(anyString())).thenReturn(false);
        Vehicle vehicle = buildVehicle(VehicleType.CAR);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        // Act
        VehicleDto result = vehicleService.create(request);

        // Assert
        assertEquals("WB34AB1234", result.getVehicleNumber());
        assertEquals(VehicleType.CAR, result.getVehicleType());
        verify(vehicleRepository).existsById(eq("WB34AB1234"));
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void create_shouldThrowConflictException_whenVehicleAlreadyExists() {
        // Arrange
        CreateVehicleRequest request = buildCreateVehicleRequest();
        when(vehicleRepository.existsById(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () -> vehicleService.create(request));
        verify(vehicleRepository).existsById(eq("WB34AB1234"));
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    void delete_shouldRemoveVehicle_whenVehicleExists() throws Exception {
        // Arrange
        Vehicle vehicle = buildVehicle(VehicleType.CAR);
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.of(vehicle));

        // Act
        vehicleService.delete("WB34AB1234");

        // Assert
        verify(vehicleRepository).findById(eq("WB34AB1234"));
        verify(vehicleRepository).delete(vehicle);
    }

    @Test
    void delete_shouldThrowResourceNotFoundException_whenVehicleDoesNotExist() {
        // Arrange
        when(vehicleRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> vehicleService.delete("WB34AB1234"));
        verify(vehicleRepository).findById(eq("WB34AB1234"));
        verify(vehicleRepository, never()).delete(any(Vehicle.class));
    }
}
