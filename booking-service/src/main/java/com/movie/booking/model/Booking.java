package com.movie.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false)
    private Long showtimeId;
    
    @Column(nullable = false)
    private Integer numberOfSeats;
    
    @Column(nullable = false)
    private Double totalPrice;
    
    @Column(nullable = false)
    private LocalDateTime bookingTime;
    
    @Column(nullable = false)
    private String status;
}
