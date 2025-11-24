#!/bin/bash

# Movie Ticket Booking System - Stop All Services Script

echo "Stopping Movie Ticket Booking System..."
echo "========================================"

LOG_DIR="./logs"

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

# Function to stop a service
stop_service() {
    local service_name=$1
    local pid_file=$LOG_DIR/$service_name.pid
    
    if [ -f $pid_file ]; then
        local pid=$(cat $pid_file)
        echo -e "Stopping $service_name (PID: $pid)..."
        kill $pid 2>/dev/null
        rm $pid_file
        echo -e "${GREEN}  Stopped${NC}"
    else
        echo -e "${RED}  No PID file found for $service_name${NC}"
    fi
}

# Stop services in reverse order
stop_service "api-gateway"
stop_service "booking-service"
stop_service "showtime-service"
stop_service "movie-service"
stop_service "user-service"
stop_service "eureka-server"

echo ""
echo "All services stopped."
echo ""
echo "Logs are preserved in: $LOG_DIR/"
echo "To view logs: tail -f $LOG_DIR/<service-name>.log"
echo ""
