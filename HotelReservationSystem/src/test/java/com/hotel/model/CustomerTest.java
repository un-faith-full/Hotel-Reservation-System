package com.hotel.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    
    @Test
    void createCustomer_ValidParameters_ShouldCreateCustomer() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        
        assertEquals("CUST001", customer.getCustomerId());
        assertEquals("John Doe", customer.getName());
        assertEquals("john@email.com", customer.getEmail());
        assertTrue(customer.getBookings().isEmpty());
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    void createCustomer_InvalidCustomerId_ShouldThrowException(String customerId) {
        assertThrows(IllegalArgumentException.class,
            () -> new Customer(customerId, "John Doe", "john@email.com"));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    void createCustomer_InvalidName_ShouldThrowException(String name) {
        assertThrows(IllegalArgumentException.class,
            () -> new Customer("CUST001", name, "john@email.com"));
    }
    
    @Test
    void createCustomer_InvalidEmail_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Customer("CUST001", "John Doe", "invalidemail"));
    }
    
    @Test
    void addBooking_ValidBooking_ShouldAddToList() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        
        java.time.LocalDate checkIn = java.time.LocalDate.now().plusDays(1);
        java.time.LocalDate checkOut = java.time.LocalDate.now().plusDays(3);
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        
        assertEquals(1, customer.getBookings().size());
    }
    
    @Test
    void addBooking_NullBooking_ShouldThrowException() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        
        assertThrows(IllegalArgumentException.class,
            () -> customer.addBooking(null));
    }
}
