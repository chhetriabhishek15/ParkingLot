package com.example.parkingLot.exception;

public class TicketAlreadyClosedException extends Exception {
    public TicketAlreadyClosedException(String message) {
        super(message);
    }
}
