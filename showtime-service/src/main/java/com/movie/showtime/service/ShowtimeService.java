package com.movie.showtime.service;

import com.movie.showtime.entity.Showtime;
import com.movie.showtime.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public Optional<Showtime> getShowtimeById(Long id) {
        return showtimeRepository.findById(id);
    }

    public Showtime createShowtime(Showtime showtime) {
        return showtimeRepository.save(showtime);
    }

    public Showtime updateShowtime(Long id, Showtime showtimeDetails) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        showtime.setMovieId(showtimeDetails.getMovieId());
        showtime.setStartTime(showtimeDetails.getStartTime());
        showtime.setAvailableSeats(showtimeDetails.getAvailableSeats());
        return showtimeRepository.save(showtime);
    }

    public void deleteShowtime(Long id) {
        showtimeRepository.deleteById(id);
    }

    @Transactional
    public Showtime reduceSeats(Long id, Integer count) {
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));
        
        if (showtime.getAvailableSeats() < count) {
            throw new RuntimeException("Not enough available seats");
        }
        
        showtime.setAvailableSeats(showtime.getAvailableSeats() - count);
        return showtimeRepository.save(showtime);
    }
}
