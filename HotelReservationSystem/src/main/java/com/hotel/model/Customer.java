package com.hotel.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private final String customerId;
    private final String name;
    private final String email;
    private final List<Booking> bookings;

    public Customer(String customerId, String name, String email) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.bookings = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public void addBooking(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking cannot be null");
        }
        bookings.add(booking);
    }
}
