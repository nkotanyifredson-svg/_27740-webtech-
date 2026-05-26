package com.auca.backend.service;

import com.auca.backend.model.Booking;
import com.auca.backend.model.Room;
import com.auca.backend.repository.BookingRepository;
import com.auca.backend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {
        Room room = roomRepository.findById(booking.getRoomId()).orElse(null);

        if (room == null) {
            throw new RuntimeException("Room not found");
        }

        if (!room.getAvailable()) {
            throw new RuntimeException("Room is already booked");
        }

        room.setAvailable(false);
        roomRepository.save(room);

        booking.setReleased(false);
        return bookingRepository.save(booking);
    }

    public Booking releaseBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);

        if (booking == null) {
            return null;
        }

        booking.setReleased(true);
        bookingRepository.save(booking);

        Room room = roomRepository.findById(booking.getRoomId()).orElse(null);
        if (room != null) {
            room.setAvailable(true);
            roomRepository.save(room);
        }

        return booking;
    }

    public void cancelBooking(UUID id) {
        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking != null) {
            Room room = roomRepository.findById(booking.getRoomId()).orElse(null);
            if (room != null) {
                room.setAvailable(true);
                roomRepository.save(room);
            }
            bookingRepository.deleteById(id);
        }
    }
}
