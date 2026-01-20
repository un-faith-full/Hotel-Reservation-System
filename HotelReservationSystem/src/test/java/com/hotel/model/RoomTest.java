package com.hotel.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    
    @Test
    void createRoom_ValidParameters_ShouldCreateRoom() {
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        
        assertEquals("101", room.getRoomNumber());
        assertEquals(RoomType.SINGLE, room.getType());
        assertEquals(100.0, room.getPricePerNight());
        assertTrue(room.isAvailable());
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    void createRoom_InvalidRoomNumber_ShouldThrowException(String roomNumber) {
        assertThrows(IllegalArgumentException.class, 
            () -> new Room(roomNumber, RoomType.SINGLE, 100.0));
    }
    
    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0})
    void createRoom_InvalidPrice_ShouldThrowException(double price) {
        assertThrows(IllegalArgumentException.class,
            () -> new Room("101", RoomType.SINGLE, price));
    }
    
    @Test
    void bookRoom_WhenAvailable_ShouldMarkAsBooked() {
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        room.bookRoom();
        
        assertFalse(room.isAvailable());
    }
    
    @Test
    void bookRoom_WhenAlreadyBooked_ShouldThrowException() {
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        room.bookRoom();
        
        assertThrows(IllegalStateException.class, room::bookRoom);
    }

    @Test
    void releaseRoom_WhenBooked_ShouldMakeAvailable() {
        Room room = new Room("101", RoomType.SINGLE, 100.0);
        room.bookRoom();
        room.releaseRoom();
        
        assertTrue(room.isAvailable());
    }
}
