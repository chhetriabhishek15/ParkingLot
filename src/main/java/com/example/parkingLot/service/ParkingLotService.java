package com.example.parkingLot.service;

import com.example.parkingLot.dto.*;
import com.example.parkingLot.enums.PaymentStatus;
import com.example.parkingLot.enums.TicketStatus;
import com.example.parkingLot.repository.MaintenanceBoardRepository;
import com.example.parkingLot.repository.PaymentRepository;
import com.example.parkingLot.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private static final Logger log = LoggerFactory.getLogger(ParkingLotService.class);

    private final ParkingFloorService floorService;
    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final MaintenanceBoardRepository maintenanceRepository;


    @Transactional(readOnly = true)
    public LotSummaryDto getParkingLotSummary() {
        List<FloorSummaryDto> floorSummaryDtos = floorService.getAllFloors();

        long totalSpots = floorSummaryDtos.stream().mapToLong(FloorSummaryDto::getTotalSpots).sum();
        long totalAvailable = floorSummaryDtos.stream().mapToLong(FloorSummaryDto::getAvailableSpots).sum();
        long totalOccupied = floorSummaryDtos.stream().mapToLong(FloorSummaryDto::getOccupiedSpots).sum();

        long activeTickets = ticketRepository.countByParkingTicketStatus(TicketStatus.ACTIVE);

        Double revenue = paymentRepository.sumAmountByPaymentStatus(PaymentStatus.COMPLETED);
        double totalRevenue = revenue == null ? 0.0 : revenue;

        long maintenanceInProgress = maintenanceRepository.countByEndTimeIsNull();

        LotSummaryDto dto = LotSummaryDto.builder()
                .lotName("ParkingLot Salua")
                .totalSpots(totalSpots)
                .totalAvailableSpots(totalAvailable)
                .totalOccupiedSpots(totalOccupied)
                .activeTickets(activeTickets)
                .totalRevenueCollected(totalRevenue)
                .maintenanceInProgress(maintenanceInProgress)
                .floors(floorSummaryDtos)
                .build();

        log.info("Lot summary computed: spots={} available={} occupied={}", totalSpots, totalAvailable, totalOccupied);

        return dto;
    }

    @Transactional(readOnly = true)
    public List<FloorSummaryDto> getFloors() {
        return floorService.getAllFloors();
    }
}
