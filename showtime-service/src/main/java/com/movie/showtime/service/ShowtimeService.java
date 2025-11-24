package com.movie.showtime.service;

import com.movie.showtime.exception.InvalidOperationException;
import com.movie.showtime.exception.ResourceNotFoundException;
import com.movie.showtime.model.Showtime;
import com.movie.showtime.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime not found with id: " + id));
    }

    public List<Showtime> getShowtimesByMovieId(Long movieId) {
        return showtimeRepository.findByMovieId(movieId);
    }

    public Showtime createShowtime(Showtime showtime) {
        showtime.setAvailableSeats(showtime.getTotalSeats());
        return showtimeRepository.save(showtime);
    }

    public Showtime updateShowtime(Long id, Showtime showtimeDetails) {
        Showtime showtime = getShowtimeById(id);
        
        Integer reservedSeats = showtime.getTotalSeats() - showtime.getAvailableSeats();
        if (showtimeDetails.getTotalSeats() < reservedSeats) {
            throw new InvalidOperationException(
                "Cannot reduce total seats below reserved seats. Reserved: " + reservedSeats + 
                ", Requested total: " + showtimeDetails.getTotalSeats());
        }
        
        showtime.setMovieId(showtimeDetails.getMovieId());
        showtime.setShowDateTime(showtimeDetails.getShowDateTime());
        showtime.setTheater(showtimeDetails.getTheater());
        showtime.setTotalSeats(showtimeDetails.getTotalSeats());
        showtime.setPrice(showtimeDetails.getPrice());
        return showtimeRepository.save(showtime);
    }

    public void deleteShowtime(Long id) {
        Showtime showtime = getShowtimeById(id);
        showtimeRepository.delete(showtime);
    }

    public synchronized boolean reserveSeats(Long showtimeId, Integer numberOfSeats) {
        Showtime showtime = getShowtimeById(showtimeId);
        
        if (showtime.getAvailableSeats() < numberOfSeats) {
            throw new InvalidOperationException("Not enough available seats. Available: " + 
                    showtime.getAvailableSeats() + ", Requested: " + numberOfSeats);
        }
        
        showtime.setAvailableSeats(showtime.getAvailableSeats() - numberOfSeats);
        showtimeRepository.save(showtime);
        return true;
    }

    public synchronized void releaseSeats(Long showtimeId, Integer numberOfSeats) {
        Showtime showtime = getShowtimeById(showtimeId);
        
        if (showtime.getAvailableSeats() + numberOfSeats > showtime.getTotalSeats()) {
            throw new InvalidOperationException(
                "Cannot release more seats than total. Total: " + showtime.getTotalSeats() + 
                ", Currently available: " + showtime.getAvailableSeats() + 
                ", Attempting to release: " + numberOfSeats);
        }
        
        showtime.setAvailableSeats(showtime.getAvailableSeats() + numberOfSeats);
        showtimeRepository.save(showtime);
    }
}
