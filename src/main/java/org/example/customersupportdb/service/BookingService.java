package org.example.customersupportdb.service;


import org.example.customersupportdb.bean.Booking;

public interface BookingService {

    Booking getBookingDetails(String bookingNumber, String customerName, String customerSurname);

    void cancelBooking(String bookingNumber, String customerName, String customerSurname);

    Booking save(Booking booking);

    Booking findBookingByBookingNumber(String bookingNumber);
}
