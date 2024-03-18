package org.example.customersupportdb.dao;

import org.example.customersupportdb.bean.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDao extends JpaRepository<Booking, Long>{

    Booking findBookingByBookingNumber(String bookingNumber);
    Booking findBookingByBookingNumberAndCustomer_NameAndCustomer_Surname(String bookingNumber, String name, String surname);
}
