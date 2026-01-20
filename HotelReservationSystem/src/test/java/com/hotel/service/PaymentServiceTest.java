package com.hotel.service;

import com.hotel.model.*;
import com.hotel.exception.InvalidPaymentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {
    private PaymentService paymentService;
    private Customer customer;
    private Room room;
    private Booking booking;
    
    @BeforeEach
    void setUp() {
        paymentService = new PaymentService();
        customer = new Customer("CUST001", "John Doe", "john@email.com");
        room = new Room("101", RoomType.SINGLE, 100.0);
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(3);
        booking = new Booking("BOOK001", customer, room, checkIn, checkOut);
    }
    
    @Test
    void createPayment_ValidParameters_ShouldCreatePayment() {
        Payment payment = paymentService.createPayment("PAY001", booking, 200.0);
        
        assertEquals("PAY001", payment.getPaymentId());
        assertEquals(booking, payment.getBooking());
        assertEquals(200.0, payment.getAmount());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }
    
    @Test
    void createPayment_NullBooking_ShouldThrowException() {
        assertThrows(InvalidPaymentException.class,
            () -> paymentService.createPayment("PAY001", null, 200.0));
    }
    
    @Test
    void createPayment_InvalidAmount_ShouldThrowException() {
        assertThrows(InvalidPaymentException.class,
            () -> paymentService.createPayment("PAY001", booking, 0.0));
    }
    
    @Test
    void processPayment_ValidPayment_ShouldMarkAsCompleted() {
        Payment payment = paymentService.createPayment("PAY001", booking, 200.0);
        
        paymentService.processPayment(payment);
        
        assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }
    
    @Test
    void processPayment_NullPayment_ShouldThrowException() {
        assertThrows(InvalidPaymentException.class,
            () -> paymentService.processPayment(null));
    }
    
    @Test
    void validatePayment_CompletedPayment_ShouldReturnTrue() {
        Payment payment = paymentService.createPayment("PAY001", booking, 200.0);
        paymentService.processPayment(payment);
        
        assertTrue(paymentService.validatePayment(payment));
    }
    
    @Test
    void validatePayment_PendingPayment_ShouldReturnFalse() {
        Payment payment = paymentService.createPayment("PAY001", booking, 200.0);
        
        assertFalse(paymentService.validatePayment(payment));
    }
    
    @Test
    void validatePayment_NullPayment_ShouldReturnFalse() {
        assertFalse(paymentService.validatePayment(null));
    }
}
