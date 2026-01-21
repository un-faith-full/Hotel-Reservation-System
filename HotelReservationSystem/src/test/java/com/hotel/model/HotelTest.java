package com.hotel.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class HotelTest {
    
    @Test
    void createHotel_ValidParameters_ShouldCreateHotel() {
        Hotel hotel = new Hotel("HOTEL001", "Grand Plaza", "New York");
        
        assertEquals("HOTEL001", hotel.getHotelId());
        assertEquals("Grand Plaza", hotel.getName());
        assertEquals("New York", hotel.getLocation());
        assertTrue(hotel.getRooms().isEmpty());
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    void createHotel_InvalidHotelId_ShouldThrowException(String hotelId) {
        assertThrows(IllegalArgumentException.class,
            () -> new Hotel(hotelId, "Grand Plaza", "New York"));
    }
    
    @Test
    void addRoom_ValidRoom_ShouldAddToList() {
        Hotel hotel = new Hotel("HOTEL001", "Mega Plaza", "New York");
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        
        hotel.addRoom(room);
        
        assertEquals(1, hotel.getRooms().size());
    }
    
    @Test
    void addRoom_NullRoom_ShouldThrowException() {
        Hotel hotel = new Hotel("HOTEL001", "Grand Plaza", "New York");
        
        assertThrows(IllegalArgumentException.class, () -> hotel.addRoom(null));
    }
    
    @Test
    void findAvailableRooms_WithAvailableRooms_ShouldReturnAvailable() {
        Hotel hotel = new Hotel("HOTEL001", "Grand Plaza", "New York");
        Room room1 = new Room("101", RoomType.SINGLE, 100.0);
        Room room2 = new Room("102", RoomType.SINGLE, 100.0);
        Room room3 = new Room("103", RoomType.DOUBLE, 150.0);
        
        hotel.addRoom(room1);
        hotel.addRoom(room2);
        hotel.addRoom(room3);
        
        var availableRooms = hotel.findAvailableRooms(RoomType.SINGLE);
        
        assertEquals(2, availableRooms.size());
    }
    
    @Test
    void findAvailableRooms_WithBookedRooms_ShouldNotReturnBooked() {
        Hotel hotel = new Hotel("HOTEL001", "Grand Plaza", "New York");
        Room room1 = new Room("101", RoomType.SINGLE, 100.0);
        Room room2 = new Room("102", RoomType.SINGLE, 100.0);
        
        hotel.addRoom(room1);
        hotel.addRoom(room2);
        
        room1.bookRoom();
        
        var availableRooms = hotel.findAvailableRooms(RoomType.SINGLE);
        
        assertEquals(1, availableRooms.size());
    }
}
