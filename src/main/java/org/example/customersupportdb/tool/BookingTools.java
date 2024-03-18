package org.example.customersupportdb.tool;

import dev.langchain4j.agent.tool.Tool;
import org.example.customersupportdb.bean.Booking;
import org.example.customersupportdb.service.BookingService;
import org.example.customersupportdb.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingTools {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private CustomerService customerService;

    @Tool
    public Booking getBookingDetails(String bookingNumber, String customerName, String customerSurname) {
        System.out.println("==========================================================================================");
        System.out.printf("[Tool]: Getting details for booking %s for %s %s...%n", bookingNumber, customerName, customerSurname);
        System.out.println("==========================================================================================");

        Booking bookingDetails = bookingService.getBookingDetails(bookingNumber, customerName, customerSurname);
        return bookingDetails;
    }

    @Tool
    public void cancelBooking(String bookingNumber, String customerName, String customerSurname) {
        System.out.println("==========================================================================================");
        System.out.printf("[Tool]: Cancelling booking %s for %s %s...%n", bookingNumber, customerName, customerSurname);
        System.out.println("==========================================================================================");

        bookingService.cancelBooking(bookingNumber, customerName, customerSurname);
    }
}
