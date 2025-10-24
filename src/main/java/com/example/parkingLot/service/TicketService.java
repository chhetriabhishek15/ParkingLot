package com.example.parkingLot.service;

import com.example.parkingLot.dto.*;
import com.example.parkingLot.entity.*;
import com.example.parkingLot.enums.*;
import com.example.parkingLot.exception.*;
import com.example.parkingLot.repository.*;
import com.example.parkingLot.utils.FeeCalculator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository spotRepository;
    private final ParkingSpotService parkingSpotService;
    private final PaymentRepository paymentRepository;


    public TicketDto get(Long ticketId) throws TicketNotFoundException {
        Ticket ticketDto = ticketRepository.findById(ticketId)
                .orElseThrow(()->new TicketNotFoundException("Ticket not found: " + ticketId));

        return toDto(ticketDto);
    }

    @Transactional
    public TicketDto createEntry(CreateTicketRequest request)
            throws ResourceNotFoundException, ConflictException {
        String vehicleNumber = request.getVehicleNumber().trim();

        Vehicle vehicle = vehicleRepository.findById(vehicleNumber)
                .orElseGet(()->{
                    Vehicle newVehicle = Vehicle.builder()
                            .vehicleNumber(vehicleNumber)
                            .vehicleType(request.getVehicleType())
                            .build();
                    log.info("New vehicle registered: {}", vehicleNumber);
                    return vehicleRepository.save(newVehicle);
                });


        ParkingSpot candidate = spotRepository
                .findFirstByParkingSpotTypeAndIsOccupiedFalseAndParkingSpotStatus(
                        request.getRequestedSpotType(),
                        ParkingSpotStatus.AVAILABLE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No available spot for type: " + request.getRequestedSpotType()));


        ParkingSpot occupied = parkingSpotService.occupySpotEntityWithRetry(candidate.getSpotId(),
                vehicleNumber,3);

        Ticket ticket = Ticket.builder()
                .vehicle(vehicle)
                .parkingSpot(occupied)
                .entryGateType(request.getEntryGateType())
                .entryTime(LocalDateTime.now())
                .parkingTicketStatus(TicketStatus.ACTIVE)
                .fee(0.0)
                .build();

        Ticket saved = ticketRepository.save(ticket);
        log.info("Ticket created: id={} vehicle={}", saved.getTicketId(), vehicleNumber);

        return toDto(saved);
    }

    @Transactional
    public TicketDto exit(@Valid ExitTicketRequest req)
            throws TicketNotFoundException, TicketAlreadyClosedException, ResourceNotFoundException {
        Ticket ticket = null;

        if (req.getTicketId()!=null){
            ticket = ticketRepository.findByTicketIdAndParkingTicketStatus(req.getTicketId(), TicketStatus.ACTIVE)
                    .orElseThrow(()-> new TicketNotFoundException("Active ticket not found with id: "
                    + req.getTicketId()));
        } else if (req.getVehicleNumber() != null && !req.getVehicleNumber().isBlank()) {
            ticket = ticketRepository.findByVehicle_VehicleNumberAndParkingTicketStatus(
                    req.getVehicleNumber(), TicketStatus.ACTIVE)
                    .orElseThrow(() -> new TicketNotFoundException("Active ticket not found for vehicle: "
                    + req.getVehicleNumber()));
        } else {
            throw new IllegalArgumentException("Either ticketId or vehicleNumber must be provided");
        }

        if (ticket.getParkingTicketStatus() != TicketStatus.ACTIVE) {
            throw new TicketAlreadyClosedException("Ticket is not active: " + ticket.getTicketId());
        }

        LocalDateTime exitTime = LocalDateTime.now();
        double fee = FeeCalculator.calculateFee(ticket.getEntryTime(), exitTime,
                ticket.getVehicle().getVehicleType());

        Payment payment = Payment.builder()
                .amount(fee)
                .paymentMethod(req.getPaymentMethod())
                .paymentStatus(PaymentStatus.COMPLETED)
                .processedAt(LocalDateTime.now())
                .ticket(ticket)
                .build();

        paymentRepository.save(payment);

        ParkingSpot spot = ticket.getParkingSpot();
        parkingSpotService.freeParkingSpot(spot.getSpotId());

        ticket.setExitTime(exitTime);
        ticket.setFee(fee);
        ticket.setParkingTicketStatus(TicketStatus.CLOSED);
        ticket.setExitGateType(req.getExitGateType());
        ticket.setPayment(payment);
        Ticket updated = ticketRepository.save(ticket);

        log.info("Ticket closed id={} fee={} vehicle={}",
                updated.getTicketId(), fee, ticket.getVehicle().getVehicleNumber());

        return toDto(updated);

    }

    private TicketDto toDto(Ticket t) {
        return TicketDto.builder()
                .ticketId(t.getTicketId())
                .vehicleNumber(t.getVehicle() != null ? t.getVehicle().getVehicleNumber() : null)
                .spotId(t.getParkingSpot() != null ? t.getParkingSpot().getSpotId() : null)
                .spotNumber(t.getParkingSpot() != null ? t.getParkingSpot().getSpotNumber() : null)
                .entryGateType(t.getEntryGateType())
                .exitGateType(t.getExitGateType())
                .entryTime(t.getEntryTime())
                .exitTime(t.getExitTime())
                .status(t.getParkingTicketStatus())
                .fee(t.getFee())
                .build();

    }
}
