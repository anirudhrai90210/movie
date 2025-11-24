# Movie Ticketing System

A minimal Spring Boot microservice-based Movie Ticketing System with no authentication and no JWT.

## Architecture

This system consists of six microservices:

1. **Eureka Discovery Server** (Port 8761) - Service registry
2. **API Gateway** (Port 8080) - Routes requests to microservices
3. **User Service** (Port 8081) - User management with MySQL
4. **Movie Service** (Port 8082) - Movie catalog with MySQL
5. **Showtime Service** (Port 8083) - Showtime management with MySQL
6. **Booking Service** (Port 8084) - Booking management with MySQL and FeignClient

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- Spring Cloud 2022.0.4
- Maven
- MySQL
- Netflix Eureka (Service Discovery)
- Spring Cloud Gateway
- OpenFeign (Inter-service communication)

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

## MySQL Setup

1. Install and start MySQL server
2. The application will automatically create the required databases:
   - `user_db`
   - `movie_db`
   - `showtime_db`
   - `booking_db`

3. Default MySQL credentials (update in application.properties if different):
   - Username: `root`
   - Password: `root`

## Build and Run

### Build All Services

From the root directory:

```bash
mvn clean install
```

### Run Services (in order)

**1. Start Eureka Discovery Server:**
```bash
cd eureka-server
mvn spring-boot:run
```
Access at: http://localhost:8761

**2. Start API Gateway:**
```bash
cd api-gateway
mvn spring-boot:run
```
Access at: http://localhost:8080

**3. Start User Service:**
```bash
cd user-service
mvn spring-boot:run
```

**4. Start Movie Service:**
```bash
cd movie-service
mvn spring-boot:run
```

**5. Start Showtime Service:**
```bash
cd showtime-service
mvn spring-boot:run
```

**6. Start Booking Service:**
```bash
cd booking-service
mvn spring-boot:run
```

Wait for all services to register with Eureka (check http://localhost:8761).

## API Endpoints

All endpoints are accessible through the API Gateway at `http://localhost:8080`.

### User Service (`/users`)

- **GET** `/users` - Get all users
- **GET** `/users/{id}` - Get user by ID
- **POST** `/users` - Create user
  ```json
  {
    "name": "John Doe",
    "email": "john@example.com"
  }
  ```
- **PUT** `/users/{id}` - Update user
- **DELETE** `/users/{id}` - Delete user

### Movie Service (`/movies`)

- **GET** `/movies` - Get all movies
- **GET** `/movies/{id}` - Get movie by ID
- **POST** `/movies` - Create movie
  ```json
  {
    "title": "Inception",
    "genre": "Sci-Fi",
    "duration": 148
  }
  ```
- **PUT** `/movies/{id}` - Update movie
- **DELETE** `/movies/{id}` - Delete movie

### Showtime Service (`/showtimes`)

- **GET** `/showtimes` - Get all showtimes
- **GET** `/showtimes/{id}` - Get showtime by ID
- **POST** `/showtimes` - Create showtime
  ```json
  {
    "movieId": 1,
    "startTime": "2024-12-25T18:00:00",
    "availableSeats": 100
  }
  ```
- **PUT** `/showtimes/{id}` - Update showtime
- **DELETE** `/showtimes/{id}` - Delete showtime
- **PUT** `/showtimes/{id}/reduce?count=x` - Reduce available seats

### Booking Service (`/bookings`)

- **GET** `/bookings` - Get all bookings
- **GET** `/bookings/user/{userId}` - Get bookings by user ID
- **POST** `/bookings` - Create booking (checks seats, reduces them, saves booking)
  ```json
  {
    "userId": 1,
    "showtimeId": 1,
    "numberOfSeats": 2
  }
  ```

## Example Usage Flow

1. Create a user:
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@example.com"}'
```

2. Create a movie:
```bash
curl -X POST http://localhost:8080/movies \
  -H "Content-Type: application/json" \
  -d '{"title": "Inception", "genre": "Sci-Fi", "duration": 148}'
```

3. Create a showtime:
```bash
curl -X POST http://localhost:8080/showtimes \
  -H "Content-Type: application/json" \
  -d '{"movieId": 1, "startTime": "2024-12-25T18:00:00", "availableSeats": 100}'
```

4. Create a booking (automatically checks and reduces seats):
```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "showtimeId": 1, "numberOfSeats": 2}'
```

5. Get user's bookings:
```bash
curl http://localhost:8080/bookings/user/1
```

## Project Structure

```
movie-ticketing-system/
├── pom.xml (parent)
├── eureka-server/
├── api-gateway/
├── user-service/
├── movie-service/
├── showtime-service/
└── booking-service/
```

## Features

- **Service Discovery**: All microservices register with Eureka Server
- **API Gateway**: Single entry point for all client requests
- **Inter-service Communication**: Booking Service uses OpenFeign to communicate with Showtime Service
- **Database per Service**: Each microservice has its own MySQL database
- **No Authentication**: Simplified setup without JWT or authentication mechanisms
- **Transaction Management**: Booking workflow ensures data consistency

## Notes

- Make sure MySQL is running before starting the services
- Wait for services to register with Eureka before testing
- Services start in a specific order to ensure proper discovery
- Check Eureka dashboard to verify all services are registered