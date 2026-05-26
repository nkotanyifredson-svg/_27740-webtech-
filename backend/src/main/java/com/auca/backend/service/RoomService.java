package com.auca.backend.service;

import com.auca.backend.model.Room;
import com.auca.backend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(UUID id) {
        return roomRepository.findById(id).orElse(null);
    }

    public Room createRoom(Room room) {
        room.setAvailable(true);
        return roomRepository.save(room);
    }

    public Room updateRoom(UUID id, Room roomDetails) {
        Room room = roomRepository.findById(id).orElse(null);
        if (room == null) return null;
        room.setName(roomDetails.getName());
        room.setCapacity(roomDetails.getCapacity());
        room.setLocation(roomDetails.getLocation());
        room.setAvailable(roomDetails.getAvailable());
        return roomRepository.save(room);
    }

    public boolean deleteRoom(UUID id) {
        if (!roomRepository.existsById(id)) return false;
        roomRepository.deleteById(id);
        return true;
    }
}
