package com.example.parkingLot.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError("NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("CONFLICT", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation failed: {}", msg);
        return ResponseEntity.badRequest().body(new ApiError("VALIDATION_ERROR", msg));
    }

    @ExceptionHandler(SpotAlreadyOccupiedException.class)
    public ResponseEntity<ApiError> handleSpotOccupied(SpotAlreadyOccupiedException ex) {
        log.warn("Spot already occupied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("SPOT_OCCUPIED", ex.getMessage()));
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ApiError> handleTicketNotFound(TicketNotFoundException ex) {
        log.warn("Ticket not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError("TICKET_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(TicketAlreadyClosedException.class)
    public ResponseEntity<ApiError> handleTicketAlreadyClosed(TicketAlreadyClosedException ex) {
        log.warn("Ticket already closed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError("TICKET_ALREADY_CLOSED", ex.getMessage()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("INTERNAL_ERROR", "An unexpected error occurred"));
    }

    public static record ApiError(String code, String message) {}
}
