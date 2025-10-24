package com.example.parkingLot.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_shouldReturnNotFoundStatus() {
        ResourceNotFoundException ex = new ResourceNotFoundException("not found");
        ResponseEntity<?> response = handler.handleNotFound(ex);
        Object body = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("NOT_FOUND", getCode(body));
        assertEquals("not found", getMessage(body));
    }

    @Test
    void handleConflict_shouldReturnConflictStatus() {
        ConflictException ex = new ConflictException("conflict");
        ResponseEntity<?> response = handler.handleConflict(ex);
        Object body = response.getBody();
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("CONFLICT", getCode(body));
        assertEquals("conflict", getMessage(body));
    }

    @Test
    void handleValidation_shouldReturnBadRequestWithAggregatedMessage() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error1 = new FieldError("obj", "field1", "must not be null");
        FieldError error2 = new FieldError("obj", "field2", "must be positive");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(error1, error2));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<?> response = handler.handleValidation(ex);
        Object body = response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("VALIDATION_ERROR", getCode(body));
        assertTrue(getMessage(body).contains("field1: must not be null"));
        assertTrue(getMessage(body).contains("field2: must be positive"));
    }

    @Test
    void handleSpotOccupied_shouldReturnConflictStatus() {
        SpotAlreadyOccupiedException ex = new SpotAlreadyOccupiedException("spot occupied");
        ResponseEntity<?> response = handler.handleSpotOccupied(ex);
        Object body = response.getBody();
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("SPOT_OCCUPIED", getCode(body));
        assertEquals("spot occupied", getMessage(body));
    }

    @Test
    void handleTicketNotFound_shouldReturnNotFoundStatus() {
        TicketNotFoundException ex = new TicketNotFoundException("ticket not found");
        ResponseEntity<?> response = handler.handleTicketNotFound(ex);
        Object body = response.getBody();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("TICKET_NOT_FOUND", getCode(body));
        assertEquals("ticket not found", getMessage(body));
    }

    @Test
    void handleTicketAlreadyClosed_shouldReturnConflictStatus() {
        TicketAlreadyClosedException ex = new TicketAlreadyClosedException("ticket closed");
        ResponseEntity<?> response = handler.handleTicketAlreadyClosed(ex);
        Object body = response.getBody();
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("TICKET_ALREADY_CLOSED", getCode(body));
        assertEquals("ticket closed", getMessage(body));
    }

    // Helper methods to extract code/message from ApiError
    private String getCode(Object body) {
        if (body instanceof GlobalExceptionHandler.ApiError apiError) {
            return apiError.code();
        }
        try {
            return (String) body.getClass().getMethod("getCode").invoke(body);
        } catch (Exception e) {
            fail("Could not get code from response body: " + e.getMessage());
            return null;
        }
    }
    private String getMessage(Object body) {
        if (body instanceof GlobalExceptionHandler.ApiError apiError) {
            return apiError.message();
        }
        try {
            return (String) body.getClass().getMethod("getMessage").invoke(body);
        } catch (Exception e) {
            fail("Could not get message from response body: " + e.getMessage());
            return null;
        }
    }
}
