package com.example.parkingLot.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConflictExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        ConflictException ex = new ConflictException("conflict error");
        assertEquals("conflict error", ex.getMessage());
    }

    @Test
    void shouldBeInstanceOfException() {
        ConflictException ex = new ConflictException("msg");
        assertInstanceOf(Exception.class, ex);
    }
}

