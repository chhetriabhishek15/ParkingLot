package com.example.parkingLot.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {
    @Test
    void constructor_shouldSetMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("not found");
        assertEquals("not found", ex.getMessage());
    }

    @Test
    void shouldBeInstanceOfException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("msg");
        assertInstanceOf(Exception.class, ex);
    }
}

