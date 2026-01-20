package com.hotel.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {
    
    @Test
    void createBooking_ValidParameters_ShouldCreateBooking() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        
        assertEquals("BOOK001", booking.getBookingId());
        assertEquals(customer, booking.getCustomer());
        assertEquals(room, booking.getRoom());
        assertEquals(checkIn, booking.getCheckInDate());
        assertEquals(checkOut, booking.getCheckOutDate());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
        assertFalse(room.isAvailable());
    }
    
    @Test
    void createBooking_NullCustomer_ShouldThrowException() {
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        assertThrows(IllegalArgumentException.class,
            () -> new Booking("BOOK001", null, room, checkIn, checkOut));
    }
    
    @Test
    void createBooking_InvalidDates_ShouldThrowException() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(3);
        LocalDate checkOut = LocalDate.now().plusDays(1);
        
        assertThrows(IllegalArgumentException.class,
            () -> new Booking("BOOK001", customer, room, checkIn, checkOut));
    }
    
    @Test
    void calculateTotalPrice_ValidBooking_ShouldCalculateCorrectly() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(4);
        
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        
        assertEquals(300.0, booking.calculateTotalPrice());
    }
    
    @Test
    void cancelBooking_ValidBooking_ShouldCancelSuccessfully() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        booking.cancelBooking();
        
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertTrue(room.isAvailable());
    }
    
    @Test
    void cancelBooking_AlreadyCancelled_ShouldThrowException() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        booking.cancelBooking();
        
        assertThrows(IllegalStateException.class, booking::cancelBooking);
    }
}
