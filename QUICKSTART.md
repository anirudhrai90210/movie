# Quick Start Guide

This guide will help you get the Movie Ticket Booking System up and running in minutes.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

Verify installations:
```bash
java -version
mvn -version
```

## Step 1: Clone and Build

```bash
# Clone the repository (if not already done)
git clone https://github.com/anirudhrai90210/movie.git
cd movie

# Build all services
mvn clean install
```

## Step 2: Start Services

Open **5 separate terminal windows** and run the following commands in order:

### Terminal 1: Start Eureka Server
```bash
cd eureka-server
mvn spring-boot:run
```
Wait until you see: "Started EurekaServerApplication"
Access: http://localhost:8761

### Terminal 2: Start User Service
```bash
cd user-service
mvn spring-boot:run
```
Wait until you see: "Started UserServiceApplication"

### Terminal 3: Start Movie Service
```bash
cd movie-service
mvn spring-boot:run
```
Wait until you see: "Started MovieServiceApplication"

### Terminal 4: Start Showtime Service
```bash
cd showtime-service
mvn spring-boot:run
```
Wait until you see: "Started ShowtimeServiceApplication"

### Terminal 5: Start Booking Service
```bash
cd booking-service
mvn spring-boot:run
```
Wait until you see: "Started BookingServiceApplication"

### Terminal 6: Start API Gateway
```bash
cd api-gateway
mvn spring-boot:run
```
Wait until you see: "Started ApiGatewayApplication"

## Step 3: Verify All Services Are Running

Visit Eureka Dashboard: http://localhost:8761

You should see all 5 services registered:
- API-GATEWAY
- USER-SERVICE
- MOVIE-SERVICE
- SHOWTIME-SERVICE
- BOOKING-SERVICE

## Step 4: Test the Application

All requests go through the API Gateway at `http://localhost:8080`

### Create a User
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","phone":"1234567890"}'
```

### Create a Movie
```bash
curl -X POST http://localhost:8080/movies \
  -H "Content-Type: application/json" \
  -d '{"title":"Inception","description":"A mind-bending thriller","genre":"Sci-Fi","duration":148,"language":"English"}'
```

### Create a Showtime
```bash
curl -X POST http://localhost:8080/showtimes \
  -H "Content-Type: application/json" \
  -d '{"movieId":1,"showDateTime":"2024-01-15T18:00:00","theater":"Screen 1","totalSeats":100,"price":12.50}'
```

### Create a Booking
```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"showtimeId":1,"numberOfSeats":2,"totalPrice":25.00}'
```

### Verify Seat Reservation
```bash
curl http://localhost:8080/showtimes/1
```
You should see `"availableSeats":98` (2 seats were reserved)

## Step 5: Run the Test Suite

If you want to run all tests automatically:

```bash
# Make sure all services are running first
chmod +x test-suite.sh
./test-suite.sh
```

## Common Issues

### Port Already in Use
If you get a "Port already in use" error, either:
- Stop the process using that port
- Change the port in the service's `application.yml` file

### Services Not Registering with Eureka
- Ensure Eureka Server is running first
- Wait 30-60 seconds for services to register
- Check the Eureka dashboard at http://localhost:8761

### Connection Refused
- Ensure all dependent services are running
- Check that API Gateway is routing to the correct service names

## Accessing H2 Consoles

Each service has its own H2 database console:

- User Service: http://localhost:8081/h2-console
  - JDBC URL: `jdbc:h2:mem:userdb`
  
- Movie Service: http://localhost:8082/h2-console
  - JDBC URL: `jdbc:h2:mem:moviedb`
  
- Showtime Service: http://localhost:8083/h2-console
  - JDBC URL: `jdbc:h2:mem:showtimedb`
  
- Booking Service: http://localhost:8084/h2-console
  - JDBC URL: `jdbc:h2:mem:bookingdb`

Username: `sa`  
Password: (leave blank)

## Next Steps

- Read the [README.md](README.md) for detailed API documentation
- Check [ARCHITECTURE.md](ARCHITECTURE.md) for system architecture details
- Explore the code to understand the implementation

## Stopping the Application

Press `Ctrl+C` in each terminal window to stop the services.
Stop services in reverse order (Gateway → Services → Eureka) for a clean shutdown.
