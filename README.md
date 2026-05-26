# Student Study Room Booking System

A full-stack web application for managing and booking study rooms.

## Tech Stack

- **Backend**: Spring Boot, Spring Data JPA, Hibernate, PostgreSQL
- **Frontend**: React JS (Vite)

## Setup Instructions

### Prerequisites

- Java 21+
- Maven
- PostgreSQL
- Node.js and npm

### Database Setup

1. Create a PostgreSQL database named `study_room_booking`:
```sql
CREATE DATABASE study_room_booking;
```

2. Update database credentials in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build and run the Spring Boot application:
```bash
mvn spring-boot:run
```

The backend will run on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend/frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm run dev
```

The frontend will run on `http://localhost:5173`

## API Endpoints

### Rooms
- `GET /api/rooms` - Get all rooms
- `GET /api/rooms/{id}` - Get room by ID
- `POST /api/rooms` - Create a new room
- `PUT /api/rooms/{id}` - Update a room

### Bookings
- `GET /api/bookings` - Get all bookings
- `POST /api/bookings` - Create a new booking
- `PUT /api/bookings/{id}` - Update a booking
- `PUT /api/bookings/{id}/release` - Release a booking
- `DELETE /api/bookings/{id}` - Cancel a booking

## Business Rules

1. When a room is booked, the room's `available` status is set to `false`
2. When a booking is cancelled, the room's `available` status is set to `true`
3. When a booking is released, the booking's `released` status is set to `true` and the room's `available` status is set to `true`