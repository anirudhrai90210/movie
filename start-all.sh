#!/bin/bash

# Movie Ticket Booking System - Start All Services Script
# This script starts all microservices in the background

echo "Starting Movie Ticket Booking System..."
echo "========================================"

# Define log directory
LOG_DIR="./logs"
mkdir -p $LOG_DIR

# Color codes
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to start a service
start_service() {
    local service_name=$1
    local service_dir=$2
    local port=$3
    local wait_time=$4
    
    echo -e "${BLUE}Starting $service_name on port $port...${NC}"
    cd $service_dir
    mvn spring-boot:run > $LOG_DIR/$service_name.log 2>&1 &
    local pid=$!
    echo "$pid" > $LOG_DIR/$service_name.pid
    cd ..
    
    echo "  PID: $pid"
    echo "  Log: $LOG_DIR/$service_name.log"
    sleep $wait_time
}

# Start Eureka Server first
echo ""
echo -e "${YELLOW}Step 1: Starting Eureka Discovery Server${NC}"
start_service "eureka-server" "eureka-server" "8761" 20

echo ""
echo "Waiting for Eureka Server to fully start..."
sleep 5

# Check if Eureka is up
echo "Checking Eureka Server..."
for i in {1..10}; do
    if curl -s http://localhost:8761 > /dev/null; then
        echo -e "${GREEN}Eureka Server is up!${NC}"
        break
    fi
    echo "  Attempt $i/10: Waiting for Eureka..."
    sleep 3
done

# Start microservices
echo ""
echo -e "${YELLOW}Step 2: Starting Microservices${NC}"

start_service "user-service" "user-service" "8081" 15
start_service "movie-service" "movie-service" "8082" 15
start_service "showtime-service" "showtime-service" "8083" 15
start_service "booking-service" "booking-service" "8084" 15

# Start API Gateway last
echo ""
echo -e "${YELLOW}Step 3: Starting API Gateway${NC}"
start_service "api-gateway" "api-gateway" "8080" 15

echo ""
echo "Waiting for all services to register with Eureka..."
sleep 10

echo ""
echo -e "${GREEN}========================================"
echo "All services started successfully!"
echo "========================================${NC}"
echo ""
echo "Service URLs:"
echo "  Eureka Dashboard: http://localhost:8761"
echo "  API Gateway:      http://localhost:8080"
echo "  User Service:     http://localhost:8081"
echo "  Movie Service:    http://localhost:8082"
echo "  Showtime Service: http://localhost:8083"
echo "  Booking Service:  http://localhost:8084"
echo ""
echo "Logs are available in: $LOG_DIR/"
echo "PIDs are saved in: $LOG_DIR/*.pid"
echo ""
echo "To stop all services, run: ./stop-all.sh"
echo "To view logs: tail -f $LOG_DIR/<service-name>.log"
echo ""
echo "Run the test suite: ./test-suite.sh"
echo ""
