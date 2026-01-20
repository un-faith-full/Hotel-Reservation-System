package com.hotel.model;

import java.time.LocalDateTime;

public class Payment {
    private final String paymentId;
    private final Booking booking;
    private final double amount;
    private final LocalDateTime paymentDate;
    private PaymentStatus status;

    public Payment(String paymentId, Booking booking, double amount) {
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment ID cannot be null or empty");
        }
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.paymentDate = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void processPayment() {
        if (status == PaymentStatus.COMPLETED) {
            throw new IllegalStateException("Payment already completed");
        }
        status = PaymentStatus.COMPLETED;
    }

    public void markAsFailed() {
        status = PaymentStatus.FAILED;
    }
}
