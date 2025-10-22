package com.example.parkingLot.config;

import com.example.parkingLot.entities.*;
import com.example.parkingLot.enums.*;
import com.example.parkingLot.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@Profile("dev")
@RequiredArgsConstructor
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingFloorRepository parkingFloorRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final GateRepository gateRepository;
    private final VehicleRepository vehicleRepository;
    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final MaintenanceBoardRepository maintenanceRecordRepository;
    private final DisplayBoardRepository displayBoardRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        try {
            if (parkingLotRepository.count()>0) {
                logger.info("DataSeeder: data already present — skipping seeder.");
                return;
            }

            logger.info("DataSeeder: starting dev seeding...");

            logger.info("DataSeeder: starting parking lot seeding...");

            // ----- Parking Lot -----
            ParkingLot parkingLot = ParkingLot.builder()
                    .lotName("Central City Parking")
                    .location("Downtown Sector 5")
                    .build();
            parkingLotRepository.save(parkingLot);

            logger.info("DataSeeder: ended parking lot seeding...");


            logger.info("DataSeeder: starting parking floor seeding...");

            // ----- Floors -----
            ParkingFloor floor1 = ParkingFloor.builder()
                    .floorNumber(1)
                    .parkingFloorStatus(ParkingFloorStatus.AVAILABLE)
                    .parkingLot(parkingLot)
                    .build();

            ParkingFloor floor2 = ParkingFloor.builder()
                    .floorNumber(2)
                    .parkingFloorStatus(ParkingFloorStatus.AVAILABLE)
                    .parkingLot(parkingLot)
                    .build();

            parkingFloorRepository.saveAll(List.of(floor1, floor2));

            logger.info("DataSeeder: ending parking floor seeding...");

            logger.info("DataSeeder: starting parking spot seeding...");

            // ----- Spots (F1: 3 compact, F2: 3 large) -----
            for (int i=1;i<=3;i++) {
                ParkingSpot parkingSpot = ParkingSpot.builder()
                        .spotNumber("F1-S" + i)
                        .parkingSpotType(ParkingSpotType.COMPACT)
                        .parkingSpotStatus(ParkingSpotStatus.AVAILABLE)
                        .isOccupied(false)
                        .floor(floor1)
                        .build();
                parkingSpotRepository.save(parkingSpot);
            }
            for (int i = 1; i <= 3; i++) {
                ParkingSpot p = ParkingSpot.builder()
                        .spotNumber("F2-S" + i)
                        .parkingSpotType(ParkingSpotType.LARGE)
                        .parkingSpotStatus(ParkingSpotStatus.AVAILABLE)
                        .isOccupied(false)
                        .floor(floor2)
                        .build();
                parkingSpotRepository.save(p);
            }

            logger.info("DataSeeder: ending parking spot seeding...");

            logger.info("DataSeeder: starting gate seeding...");

            // ----- Gates -----
            Gate entryGate = Gate.builder().gateName("North Entry").gateType(GateType.ENTRY).parkingLot(parkingLot).build();
            Gate exitGate = Gate.builder().gateName("South Exit").gateType(GateType.EXIT).parkingLot(parkingLot).build();
            gateRepository.saveAll(List.of(entryGate, exitGate));

            logger.info("DataSeeder: ending gate seeding...");


            logger.info("DataSeeder: starting vehicles seeding...");

            // ----- Vehicles -----
            Vehicle car = Vehicle.builder().vehicleNumber("WB34AB1234").vehicleType(VehicleType.CAR).build();
            Vehicle bike = Vehicle.builder().vehicleNumber("WB34XY9876").vehicleType(VehicleType.MOTORCYCLE).build();
            vehicleRepository.saveAll(List.of(car, bike));

            logger.info("DataSeeder: ending vehicles seeding...");


            logger.info("DataSeeder: starting assign a Spot...");

            // ----- Assign a Spot -----
            Optional<ParkingSpot> maybeSpot = Optional.ofNullable(parkingSpotRepository.findBySpotNumber("F1-S1"));
            if (maybeSpot.isEmpty()){
                throw new IllegalStateException("Expected spot F1-S1 not found!");
            }
            ParkingSpot assignedSpot = maybeSpot.get();
            assignedSpot.setOccupied(true);
            assignedSpot.setParkingSpotStatus(ParkingSpotStatus.OCCUPIED);
            assignedSpot.setCurrntVehicleNumber(car.getVehicleNumber());
            assignedSpot.setLastOccupiedAt(LocalDateTime.now().minusHours(2));
            parkingSpotRepository.save(assignedSpot);

            logger.info("DataSeeder: ending assign a Spot...");

            logger.info("DataSeeder: ending parking spot seeding...");
            logger.info("DataSeeder: ending parking spot seeding...");

            logger.info("DataSeeder: ending parking spot seeding...");
            logger.info("DataSeeder: ending parking spot seeding...");

        } catch (Exception ex) {
            logger.error("DataSeeder: error during seeding", ex);
            throw ex;
        }
    }
}
