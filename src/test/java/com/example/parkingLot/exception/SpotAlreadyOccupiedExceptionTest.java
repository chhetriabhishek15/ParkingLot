package com.example.parkingLot.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SpotAlreadyOccupiedExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        SpotAlreadyOccupiedException ex = new SpotAlreadyOccupiedException("spot occupied");
        assertEquals("spot occupied", ex.getMessage());
    }

    @Test
    void shouldBeInstanceOfException() {
        SpotAlreadyOccupiedException ex = new SpotAlreadyOccupiedException("msg");
        assertInstanceOf(Exception.class, ex);
    }
}

