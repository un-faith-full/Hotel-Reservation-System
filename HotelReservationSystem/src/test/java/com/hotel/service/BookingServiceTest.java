package com.hotel.service;

import com.hotel.model.*;
import com.hotel.exception.InvalidBookingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {
    private BookingService bookingService;
    private Customer customer;
    private Room room;
    
    @BeforeEach
    void setUp() {
        bookingService = new BookingService();
        customer = new Customer("CUST001", "John Doe", "john@email.com");
        room = new Room("101", RoomType.SINGLE, 100.0);
    }
    
    @Test
    void createBooking_ValidParameters_ShouldCreateBooking() {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        Booking booking = bookingService.createBooking(
            "BOOK001", customer, room, checkIn, checkOut);
        
        assertEquals("BOOK001", booking.getBookingId());
        assertEquals(customer, booking.getCustomer());
        assertEquals(room, booking.getRoom());
        assertFalse(room.isAvailable());
    }
    
    @Test
    void createBooking_WithPastDate_ShouldThrowException() {
        LocalDate checkIn = LocalDate.now().minusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(1);
        
        assertThrows(InvalidBookingException.class,
            () -> bookingService.createBooking("BOOK001", customer, room, checkIn, checkOut));
    }
    
    @Test
    void cancelBooking_ValidBooking_ShouldCancelSuccessfully() {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        Booking booking = bookingService.createBooking(
            "BOOK001", customer, room, checkIn, checkOut);
        
        bookingService.cancelBooking(booking);
        
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertTrue(room.isAvailable());
    }
    
    @Test
    void cancelBooking_NullBooking_ShouldThrowException() {
        assertThrows(InvalidBookingException.class,
            () -> bookingService.cancelBooking(null));
    }
    
    @Test
    void calculateBookingPrice_ValidBooking_ShouldCalculateCorrectly() {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(4);
        
        Booking booking = bookingService.createBooking(
            "BOOK001", customer, room, checkIn, checkOut);
        
        double price = bookingService.calculateBookingPrice(booking);
        
        assertEquals(300.0, price);
    }
    
    @Test
    void calculateBookingPrice_NullBooking_ShouldThrowException() {
        assertThrows(InvalidBookingException.class,
            () -> bookingService.calculateBookingPrice(null));
    }
}
