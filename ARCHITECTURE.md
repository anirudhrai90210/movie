# Movie Ticket Booking System - Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                         Client/User                              │
└───────────────────────┬─────────────────────────────────────────┘
                        │
                        │ HTTP Requests
                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API Gateway (8080)                            │
│                  Spring Cloud Gateway                            │
│              ┌──────────────────────────┐                        │
│              │  Routes:                  │                       │
│              │  /users/**               │                        │
│              │  /movies/**              │                        │
│              │  /showtimes/**           │                        │
│              │  /bookings/**            │                        │
│              └──────────────────────────┘                        │
└───┬─────────────┬─────────────┬─────────────┬───────────────────┘
    │             │             │             │
    │             │             │             │
    ▼             ▼             ▼             ▼
┌─────────┐ ┌─────────┐ ┌───────────┐ ┌──────────┐
│  User   │ │  Movie  │ │ Showtime  │ │ Booking  │
│ Service │ │ Service │ │  Service  │ │ Service  │
│ (8081)  │ │ (8082)  │ │  (8083)   │ │ (8084)   │
└────┬────┘ └────┬────┘ └─────┬─────┘ └────┬─────┘
     │           │             │             │
     │           │             │             │ OpenFeign
     │           │             │◄────────────┘ (reserve/release seats)
     │           │             │
     │           │             │
     ▼           ▼             ▼             ▼
┌─────────┐ ┌─────────┐ ┌───────────┐ ┌──────────┐
│   H2    │ │   H2    │ │    H2     │ │   H2     │
│ userdb  │ │ moviedb │ │ showtimedb│ │ bookingdb│
└─────────┘ └─────────┘ └───────────┘ └──────────┘

     │           │             │             │
     └───────────┴─────────────┴─────────────┘
                        │
                        │ Register Services
                        ▼
            ┌─────────────────────────┐
            │  Eureka Server (8761)   │
            │   Service Discovery     │
            └─────────────────────────┘
```

## Service Communication Flow

### 1. Normal API Request (e.g., GET /users)
```
Client → API Gateway → User Service → H2 Database → Response
```

### 2. Booking Creation (with OpenFeign)
```
Client → API Gateway → Booking Service → OpenFeign → Showtime Service
                            │                              │
                            │                              ▼
                            │                         Reserve Seats
                            │                              │
                            ▼                              │
                        Save Booking ◄────────────────────┘
                            │
                            ▼
                        Response to Client
```

## Key Components

### Eureka Discovery Server
- Port: 8761
- Purpose: Service registration and discovery
- All microservices register with Eureka on startup

### API Gateway
- Port: 8080
- Purpose: Single entry point for all client requests
- Routes requests to appropriate microservices
- Load balancing via Eureka

### Microservices
1. **User Service** - Manages user accounts
2. **Movie Service** - Manages movie catalog
3. **Showtime Service** - Manages movie showtimes and seat availability
4. **Booking Service** - Manages bookings and communicates with Showtime Service

## Design Patterns Used

- **API Gateway Pattern**: Centralized entry point
- **Service Discovery**: Eureka for dynamic service location
- **Database per Service**: Each service has its own database
- **Circuit Breaker Ready**: OpenFeign supports Resilience4j integration
- **Centralized Exception Handling**: @RestControllerAdvice in all services
