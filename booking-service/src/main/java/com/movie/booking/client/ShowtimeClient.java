package com.movie.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "showtime-service")
public interface ShowtimeClient {
    
    @GetMapping("/showtimes/{id}")
    ShowtimeResponse getShowtime(@PathVariable("id") Long id);
    
    @PutMapping("/showtimes/{id}/reduce")
    ShowtimeResponse reduceSeats(@PathVariable("id") Long id, @RequestParam("count") Integer count);
}
