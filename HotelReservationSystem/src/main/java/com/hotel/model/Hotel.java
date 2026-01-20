package com.hotel.model;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private final String hotelId;
    private final String name;
    private final String location;
    private final List<Room> rooms;

    public Hotel(String hotelId, String name, String location) {
        if (hotelId == null || hotelId.trim().isEmpty()) {
            throw new IllegalArgumentException("Hotel ID cannot be null or empty");
        }
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.rooms = new ArrayList<>();
    }

    public String getHotelId() {
        return hotelId;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public void addRoom(Room room) {
        if (room == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        rooms.add(room);
    }

    public List<Room> findAvailableRooms(RoomType type) {
        return rooms.stream()
                .filter(room -> room.isAvailable() && room.getType() == type)
                .toList();
    }
}
