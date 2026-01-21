package com.hotel.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    
    @Test
    void createPayment_ValidParameters_ShouldCreatePayment() {
        Customer customer = new Customer("CUST001", "John Doe", "jon@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        
        Payment payment = new Payment("PAY001", booking, 200.0);
        
        assertEquals("PAY001", payment.getPaymentId());
        assertEquals(booking, payment.getBooking());
        assertEquals(200.0, payment.getAmount());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }
    
    @Test
    void createPayment_InvalidAmount_ShouldThrowException() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        
        assertThrows(IllegalArgumentException.class,
            () -> new Payment("PAY001", booking, 0.0));
    }
    
    @Test
    void processPayment_ValidPayment_ShouldMarkAsCompleted() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        Payment payment = new Payment("PAY001", booking, 200.0);
        
        payment.processPayment();
        
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }
    
    @Test
    void processPayment_AlreadyCompleted_ShouldThrowException() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        Payment payment = new Payment("PAY001", booking, 200.0);
        payment.processPayment();
        
        assertThrows(IllegalStateException.class, payment::processPayment);
    }
    
    @Test
    void markAsFailed_ValidPayment_ShouldMarkAsFailed() {
        Customer customer = new Customer("CUST001", "John Doe", "john@email.com");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        Booking booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
        Payment payment = new Payment("PAY001", booking, 200.0);
        
        payment.markAsFailed();
        
        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }
}
