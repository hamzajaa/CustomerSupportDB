package org.example.customersupportdb.exception;

public class BookingCannotBeCancelledException extends RuntimeException{
    public BookingCannotBeCancelledException(String bookingNumber) {
        super("Booking " + bookingNumber + " cannot be canceled");
    }
}
