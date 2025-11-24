package com.movie.booking.service;

import com.movie.booking.client.ShowtimeClient;
import com.movie.booking.client.ShowtimeResponse;
import com.movie.booking.entity.Booking;
import com.movie.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private ShowtimeClient showtimeClient;

    @Transactional
    public Booking createBooking(Booking booking) {
        // Get showtime details
        ShowtimeResponse showtime = showtimeClient.getShowtime(booking.getShowtimeId());
        
        // Check seat availability
        if (showtime.getAvailableSeats() < booking.getNumberOfSeats()) {
            throw new RuntimeException("Not enough available seats");
        }
        
        // Reduce seats via FeignClient
        showtimeClient.reduceSeats(booking.getShowtimeId(), booking.getNumberOfSeats());
        
        // Save booking
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}
