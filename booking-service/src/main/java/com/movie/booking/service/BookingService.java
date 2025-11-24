package com.movie.booking.service;

import com.movie.booking.client.ShowtimeClient;
import com.movie.booking.exception.BookingException;
import com.movie.booking.exception.ResourceNotFoundException;
import com.movie.booking.model.Booking;
import com.movie.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowtimeClient showtimeClient;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByShowtimeId(Long showtimeId) {
        return bookingRepository.findByShowtimeId(showtimeId);
    }

    public Booking createBooking(Booking booking) {
        try {
            Boolean reserved = showtimeClient.reserveSeats(booking.getShowtimeId(), booking.getNumberOfSeats());
            
            if (!reserved) {
                throw new BookingException("Failed to reserve seats for showtime id: " + booking.getShowtimeId());
            }
            
            booking.setBookingTime(LocalDateTime.now());
            booking.setStatus("CONFIRMED");
            return bookingRepository.save(booking);
            
        } catch (Exception e) {
            throw new BookingException("Failed to create booking: " + e.getMessage());
        }
    }

    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        
        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new BookingException("Cannot cancel booking with status: " + booking.getStatus());
        }
        
        try {
            showtimeClient.releaseSeats(booking.getShowtimeId(), booking.getNumberOfSeats());
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);
        } catch (Exception e) {
            throw new BookingException("Failed to cancel booking: " + e.getMessage());
        }
    }
}
