package org.example.customersupportdb.service.impl;


import org.example.customersupportdb.bean.Booking;
import org.example.customersupportdb.bean.Customer;
import org.example.customersupportdb.dao.BookingDao;
import org.example.customersupportdb.enums.BookingStatus;
import org.example.customersupportdb.exception.BookingCannotBeCancelledException;
import org.example.customersupportdb.exception.BookingNotFoundException;
import org.example.customersupportdb.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingDao bookingDao;

    public Booking getBookingDetails(String bookingNumber, String customerName, String customerSurname) {
        Booking bookingDetails = bookingDao.findBookingByBookingNumberAndCustomer_NameAndCustomer_Surname(bookingNumber, customerName, customerSurname);
        if (bookingDetails == null) {
            throw new BookingNotFoundException(bookingNumber);
        }
        return bookingDetails;
    }



    public void cancelBooking(String bookingNumber, String customerName, String customerSurname) {
        Booking booking = bookingDao.findBookingByBookingNumberAndCustomer_NameAndCustomer_Surname(bookingNumber, customerName, customerSurname);
        if (booking == null) {
            // Imitating cancellation
            throw new BookingCannotBeCancelledException(bookingNumber);
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingDao.save(booking);
    }

    @Override
    public Booking save(Booking booking) {
        return bookingDao.save(booking);
    }

    @Override
    public Booking findBookingByBookingNumber(String bookingNumber) {
        return bookingDao.findBookingByBookingNumber(bookingNumber);
    }

    private void ensureExists(String bookingNumber, String customerName, String customerSurname) {
        // Imitating check
        if (!(bookingNumber.equals("123-456")
                && customerName.equals("Klaus")
                && customerSurname.equals("Heisler"))) {
            throw new BookingNotFoundException(bookingNumber);
        }
    }
}
