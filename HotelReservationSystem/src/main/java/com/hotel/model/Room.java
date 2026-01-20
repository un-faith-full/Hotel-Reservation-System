package com.hotel.model;

public class Room {
    private final String roomNumber;
    private RoomType type;
    private double pricePerNight;
    private boolean isAvailable;

    public Room(String roomNumber, RoomType type, double pricePerNight) {
        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be null or empty");
        }
        if (pricePerNight <= 0) {
            throw new IllegalArgumentException("Price per night must be positive");
        }
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        if (pricePerNight <= 0) {
            throw new IllegalArgumentException("Price per night must be positive");
        }
        this.pricePerNight = pricePerNight;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void bookRoom() {
        if (!isAvailable) {
            throw new IllegalStateException("Room is already booked");
        }
        isAvailable = false;
    }

    public void releaseRoom() {
        isAvailable = true;
    }
}
