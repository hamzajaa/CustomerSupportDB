package org.example.customersupportdb.bean;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.customersupportdb.enums.BookingStatus;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookingNumber;
    private LocalDate bookingFrom;
    private LocalDate bookingTo;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    @ManyToOne
    private Customer customer;
}
