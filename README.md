# Movie Ticket Booking System

A beginner-friendly Spring Boot microservices application for a simple Movie Ticket Booking System.

## Architecture Overview

This system uses a microservices architecture with the following components:

- **Eureka Discovery Server** (Port 8761): Service registration and discovery
- **API Gateway** (Port 8080): Centralized routing for all microservices
- **User Service** (Port 8081): User management microservice
- **Movie Service** (Port 8082): Movie catalog management microservice
- **Showtime Service** (Port 8083): Movie showtime and seat management microservice
- **Booking Service** (Port 8084): Booking management with OpenFeign integration

## Key Features

- ✅ **Service Discovery**: All services register with Eureka Server
- ✅ **API Gateway**: Centralized routing using Spring Cloud Gateway
- ✅ **Microservices**: Loosely coupled, independently deployable services
- ✅ **OpenFeign Integration**: Booking Service communicates with Showtime Service
- ✅ **Centralized Exception Handling**: All services have global exception handlers
- ✅ **In-Memory Database**: H2 database for each service
- ✅ **RESTful APIs**: Complete CRUD operations for all entities

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- Spring Cloud 2022.0.4
- Spring Cloud Netflix Eureka
- Spring Cloud Gateway
- Spring Cloud OpenFeign
- Spring Data JPA
- H2 Database
- Lombok
- Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Build the Project

```bash
mvn clean install
```

### 2. Start Services (in order)

#### Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```

Wait for Eureka to start (accessible at http://localhost:8761)

#### Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```

#### Start User Service
```bash
cd user-service
mvn spring-boot:run
```

#### Start Movie Service
```bash
cd movie-service
mvn spring-boot:run
```

#### Start Showtime Service
```bash
cd showtime-service
mvn spring-boot:run
```

#### Start Booking Service
```bash
cd booking-service
mvn spring-boot:run
```

## API Endpoints

All requests should go through the API Gateway at `http://localhost:8080`

### User Service

- `GET /users` - Get all users
- `GET /users/{id}` - Get user by ID
- `POST /users` - Create a new user
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

**Example User JSON:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "1234567890"
}
```

### Movie Service

- `GET /movies` - Get all movies
- `GET /movies/{id}` - Get movie by ID
- `GET /movies/genre/{genre}` - Get movies by genre
- `POST /movies` - Create a new movie
- `PUT /movies/{id}` - Update movie
- `DELETE /movies/{id}` - Delete movie

**Example Movie JSON:**
```json
{
  "title": "Inception",
  "description": "A thief who steals corporate secrets through dream-sharing technology",
  "genre": "Sci-Fi",
  "duration": 148,
  "language": "English"
}
```

### Showtime Service

- `GET /showtimes` - Get all showtimes
- `GET /showtimes/{id}` - Get showtime by ID
- `GET /showtimes/movie/{movieId}` - Get showtimes by movie ID
- `POST /showtimes` - Create a new showtime
- `PUT /showtimes/{id}` - Update showtime
- `DELETE /showtimes/{id}` - Delete showtime
- `POST /showtimes/{id}/reserve?seats={number}` - Reserve seats
- `POST /showtimes/{id}/release?seats={number}` - Release seats

**Example Showtime JSON:**
```json
{
  "movieId": 1,
  "showDateTime": "2024-01-15T18:00:00",
  "theater": "Screen 1",
  "totalSeats": 100,
  "price": 12.50
}
```

### Booking Service

- `GET /bookings` - Get all bookings
- `GET /bookings/{id}` - Get booking by ID
- `GET /bookings/user/{userId}` - Get bookings by user ID
- `GET /bookings/showtime/{showtimeId}` - Get bookings by showtime ID
- `POST /bookings` - Create a new booking
- `DELETE /bookings/{id}` - Cancel booking

**Example Booking JSON:**
```json
{
  "userId": 1,
  "showtimeId": 1,
  "numberOfSeats": 2,
  "totalPrice": 25.00
}
```

## Service Communication

The **Booking Service** uses **OpenFeign** to communicate with the **Showtime Service**:

- When a booking is created, it calls the Showtime Service to reserve seats
- When a booking is cancelled, it calls the Showtime Service to release seats
- This demonstrates inter-service communication in a microservices architecture

## Exception Handling

All services have centralized exception handling via `@RestControllerAdvice`:

- `ResourceNotFoundException` - Returns 404 when resources are not found
- `BookingException` / `InvalidOperationException` - Returns 400 for business logic errors
- Generic `Exception` - Returns 500 for unexpected errors

Error responses include:
- `timestamp` - When the error occurred
- `message` - Error description
- `status` - HTTP status code

## Database

Each service uses its own H2 in-memory database:
- User Service: `jdbc:h2:mem:userdb`
- Movie Service: `jdbc:h2:mem:moviedb`
- Showtime Service: `jdbc:h2:mem:showtimedb`
- Booking Service: `jdbc:h2:mem:bookingdb`

H2 Console is enabled for each service at:
- User Service: http://localhost:8081/h2-console
- Movie Service: http://localhost:8082/h2-console
- Showtime Service: http://localhost:8083/h2-console
- Booking Service: http://localhost:8084/h2-console

## Testing the Application

### 1. Create a User
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","phone":"1234567890"}'
```

### 2. Create a Movie
```bash
curl -X POST http://localhost:8080/movies \
  -H "Content-Type: application/json" \
  -d '{"title":"Inception","description":"A mind-bending thriller","genre":"Sci-Fi","duration":148,"language":"English"}'
```

### 3. Create a Showtime
```bash
curl -X POST http://localhost:8080/showtimes \
  -H "Content-Type: application/json" \
  -d '{"movieId":1,"showDateTime":"2024-01-15T18:00:00","theater":"Screen 1","totalSeats":100,"price":12.50}'
```

### 4. Create a Booking
```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"showtimeId":1,"numberOfSeats":2,"totalPrice":25.00}'
```

## Project Structure

```
movie-ticket-booking/
├── eureka-server/          # Service discovery
├── api-gateway/            # API Gateway
├── user-service/           # User management
├── movie-service/          # Movie catalog
├── showtime-service/       # Showtime & seat management
├── booking-service/        # Booking with Feign client
└── pom.xml                 # Parent POM
```

## Learning Objectives

This project demonstrates:

1. **Microservices Architecture** - Independent, loosely coupled services
2. **Service Discovery** - Eureka for service registration
3. **API Gateway Pattern** - Centralized routing
4. **Inter-Service Communication** - OpenFeign for REST calls
5. **Exception Handling** - Centralized error management
6. **RESTful API Design** - Standard CRUD operations
7. **Database per Service** - Each microservice has its own database

## Future Enhancements

- Add authentication and authorization (Spring Security + JWT)
- Implement distributed tracing (Sleuth + Zipkin)
- Add circuit breakers (Resilience4j)
- Implement API rate limiting
- Add containerization (Docker)
- Implement configuration server (Spring Cloud Config)
- Add message queuing (RabbitMQ/Kafka)
- Implement caching (Redis)

## License

This project is open source and available for learning purposes.