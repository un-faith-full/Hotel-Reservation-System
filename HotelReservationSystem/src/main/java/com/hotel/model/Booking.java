package com.hotel.model;

import java.time.LocalDate;

public class Booking {
    private final String bookingId;
    private final Customer customer;
    private final Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private BookingStatus status;

    public Booking(String bookingId, Customer customer, Room room, 
                   LocalDate checkInDate, LocalDate checkOutDate) {
        if (bookingId == null || bookingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Booking ID cannot be null or empty");
        }
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        if (checkInDate == null || checkOutDate == null || 
            checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            throw new IllegalArgumentException("Invalid check-in/check-out dates");
        }
        if (!room.isAvailable()) {
            throw new IllegalStateException("Room is not available for booking");
        }
        
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = BookingStatus.CONFIRMED;
        
        room.bookRoom();
        customer.addBooking(this);
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public double calculateTotalPrice() {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights * room.getPricePerNight();
    }

    public void cancelBooking() {
        if (status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking is already cancelled");
        }
        status = BookingStatus.CANCELLED;
        room.releaseRoom();
    }
}
