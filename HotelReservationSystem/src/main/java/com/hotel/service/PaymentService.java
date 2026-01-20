package com.hotel.service;

import com.hotel.model.*;
import com.hotel.exception.InvalidPaymentException;

public class PaymentService {
    
    public Payment createPayment(String paymentId, Booking booking, double amount) {
        if (booking == null) {
            throw new InvalidPaymentException("Booking cannot be null");
        }
        if (amount <= 0) {
            throw new InvalidPaymentException("Payment amount must be positive");
        }
        
        Payment payment = new Payment(paymentId, booking, amount);
        return payment;
    }
    
    public void processPayment(Payment payment) {
        if (payment == null) {
            throw new InvalidPaymentException("Payment cannot be null");
        }
        try {
            payment.processPayment();
        } catch (IllegalStateException e) {
            throw new InvalidPaymentException("Failed to process payment: " + e.getMessage(), e);
        }
    }
    
    public boolean validatePayment(Payment payment) {
        if (payment == null) {
            return false;
        }
        return payment.getStatus() == PaymentStatus.COMPLETED;
    }
}
