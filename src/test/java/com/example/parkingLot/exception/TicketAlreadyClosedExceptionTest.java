package com.example.parkingLot.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketAlreadyClosedExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        TicketAlreadyClosedException ex = new TicketAlreadyClosedException("ticket closed");
        assertEquals("ticket closed", ex.getMessage());
    }

    @Test
    void shouldBeInstanceOfException() {
        TicketAlreadyClosedException ex = new TicketAlreadyClosedException("msg");
        assertInstanceOf(Exception.class, ex);
    }
}

