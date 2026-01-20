package com.hotel.service;

import com.hotel.model.*;
import com.hotel.exception.InvalidBookingException;
import java.time.LocalDate;
import java.util.List;

public class BookingService {
    
    public Booking createBooking(String bookingId, Customer customer, Room room, 
                                 LocalDate checkIn, LocalDate checkOut) {
        try {
            Booking booking = new Booking(bookingId, customer, room, checkIn, checkOut);
            return booking;
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new InvalidBookingException("Failed to create booking: " + e.getMessage(), e);
        }
    }
    
    public void cancelBooking(Booking booking) {
        if (booking == null) {
            throw new InvalidBookingException("Booking cannot be null");
        }
        try {
            booking.cancelBooking();
        } catch (IllegalStateException e) {
            throw new InvalidBookingException("Failed to cancel booking: " + e.getMessage(), e);
        }
    }
    
    public double calculateBookingPrice(Booking booking) {
        if (booking == null) {
            throw new InvalidBookingException("Booking cannot be null");
        }
        return booking.calculateTotalPrice();
    }
}
