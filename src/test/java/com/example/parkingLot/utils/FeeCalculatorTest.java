package com.example.parkingLot.utils;

import com.example.parkingLot.enums.VehicleType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FeeCalculatorTest {
    @Test
    void calculateFee_shouldReturnZero_whenEntryOrExitIsNull() {
        assertEquals(0.0, FeeCalculator.calculateFee(null, LocalDateTime.now(), VehicleType.CAR));
        assertEquals(0.0, FeeCalculator.calculateFee(LocalDateTime.now(), null, VehicleType.CAR));
    }

    @Test
    void calculateFee_shouldReturnZero_whenExitNotAfterEntry() {
        LocalDateTime now = LocalDateTime.now();
        assertEquals(0.0, FeeCalculator.calculateFee(now, now, VehicleType.CAR));
        assertEquals(0.0, FeeCalculator.calculateFee(now, now.minusMinutes(1), VehicleType.CAR));
    }

    @Test
    void calculateFee_shouldRoundUpToNextHour() {
        LocalDateTime entry = LocalDateTime.of(2025, 10, 25, 10, 0);
        LocalDateTime exit = entry.plusMinutes(61);
        assertEquals(20.0, FeeCalculator.calculateFee(entry, exit, VehicleType.MOTORCYCLE)); // 2 hours * 10
    }

    @Test
    void calculateFee_shouldCalculateCorrectFee_forKnownTypes() {
        LocalDateTime entry = LocalDateTime.of(2025, 10, 25, 8, 0);
        LocalDateTime exit = entry.plusHours(3);
        assertEquals(30.0, FeeCalculator.calculateFee(entry, exit, VehicleType.MOTORCYCLE));
        assertEquals(90.0, FeeCalculator.calculateFee(entry, exit, VehicleType.CAR));
        assertEquals(180.0, FeeCalculator.calculateFee(entry, exit, VehicleType.BUS));
    }

}
