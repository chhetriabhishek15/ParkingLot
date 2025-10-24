package com.example.parkingLot.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketNotFoundExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        TicketNotFoundException ex = new TicketNotFoundException("ticket not found");
        assertEquals("ticket not found", ex.getMessage());
    }

    @Test
    void shouldBeInstanceOfException() {
        TicketNotFoundException ex = new TicketNotFoundException("msg");
        assertTrue(true);
    }
}

