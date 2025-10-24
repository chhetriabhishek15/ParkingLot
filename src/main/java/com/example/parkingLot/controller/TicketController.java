package com.example.parkingLot.controller;

import com.example.parkingLot.dto.*;
import com.example.parkingLot.exception.*;
import com.example.parkingLot.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private static final Logger log = LoggerFactory.getLogger(TicketController.class);
    private final TicketService ticketService;

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketDto> get(@PathVariable Long ticketId) throws TicketNotFoundException {
        return ResponseEntity.ok(ticketService.get(ticketId));
    }

    @PostMapping("/entry")
    public ResponseEntity<TicketDto> entry(@Valid @RequestBody CreateTicketRequest request)
            throws ResourceNotFoundException, ConflictException {
        TicketDto ticketDto = ticketService.createEntry(request);
        return ResponseEntity.created(URI.create("/api/tickets/"+ticketDto.getTicketId())).body(ticketDto);
    }

    @PostMapping("/exit")
    public ResponseEntity<TicketDto> exit(@Valid @RequestBody ExitTicketRequest req)
            throws TicketNotFoundException, TicketAlreadyClosedException, ResourceNotFoundException {
        TicketDto dto = ticketService.exit(req);
        return ResponseEntity.ok(dto);
    }
}
