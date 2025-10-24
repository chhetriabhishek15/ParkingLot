package com.example.parkingLot.utils;

import com.example.parkingLot.enums.VehicleType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public final class FeeCalculator {

    private static final Map<VehicleType, Integer> RATE_PER_HOUR = Map.of(
            VehicleType.MOTORCYCLE, 10,
            VehicleType.CAR, 30,
            VehicleType.BUS, 60
    );

    private FeeCalculator() {}

    public static double calculateFee(LocalDateTime entry, LocalDateTime exit, VehicleType type) {
        if (entry == null || exit == null || !exit.isAfter(entry)) {
            return 0.0;
        }
        long minutes = Duration.between(entry, exit).toMinutes();
        long hours = minutes / 60;
        if (minutes % 60 != 0) hours += 1;
        int rate = RATE_PER_HOUR.getOrDefault(type, 30);
        return hours * rate;
    }
}
