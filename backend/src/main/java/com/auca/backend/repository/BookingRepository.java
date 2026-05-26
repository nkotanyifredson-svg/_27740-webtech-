package com.auca.backend.repository;

import com.auca.backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
