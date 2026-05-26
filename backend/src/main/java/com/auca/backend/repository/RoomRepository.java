package com.auca.backend.repository;

import com.auca.backend.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
}
