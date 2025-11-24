package com.movie.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "showtime-service")
public interface ShowtimeClient {
    
    @PostMapping("/showtimes/{id}/reserve")
    Boolean reserveSeats(@PathVariable("id") Long showtimeId, @RequestParam("seats") Integer seats);
    
    @PostMapping("/showtimes/{id}/release")
    void releaseSeats(@PathVariable("id") Long showtimeId, @RequestParam("seats") Integer seats);
}
