# Parking Lot Management System

## Overview

This project is a backend system for managing a parking lot, built with Java and Spring Boot. It provides features for parking lot setup, vehicle entry/exit, ticketing, fee calculation, and maintenance tracking. The system is modular, using repositories, services, and controllers to separate concerns and ensure maintainability.

## Features

- Parking Lot Setup: Configure parking lots, floors, spots, gates, and display boards.
- Vehicle Management: Register vehicles, assign parking spots, and track occupancy.
- Ticketing: Issue and close parking tickets for vehicles entering and exiting.
- Fee Calculation: Calculate parking fees based on vehicle type and duration.
- Maintenance: Track maintenance records for parking floors and spots.
- Display Boards: Show real-time parking availability.

## Project Structure

- `src/main/java/com/example/parkingLot/config`: Configuration classes (e.g., data seeder).
- `src/main/java/com/example/parkingLot/controller`: REST controllers for API endpoints.
- `src/main/java/com/example/parkingLot/dto`: Data transfer objects for API requests/responses.
- `src/main/java/com/example/parkingLot/entity`: JPA entities representing domain models.
- `src/main/java/com/example/parkingLot/enums`: Enum types for statuses and categories.
- `src/main/java/com/example/parkingLot/exception`: Custom exception classes.
- `src/main/java/com/example/parkingLot/repository`: Spring Data JPA repositories.
- `src/main/java/com/example/parkingLot/service`: Business logic and service classes.
- `src/main/java/com/example/parkingLot/utils`: Utility classes (e.g., fee calculator).

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle
- (Optional) Docker for database setup

### Build and Run

1. Clone the repository:
   ```
   git clone <repo-url>
   cd parkingLot
   ```

2. Build the project:
   ```
   ./gradlew build
   ```

3. Run the application:
   ```
   ./gradlew bootRun
   ```

4. Access the API at `http://localhost:8080`.

### Testing

Run all tests with:
```
./gradlew test
```
Test results are available in `build/reports/tests/test/index.html`.

## Configuration

- Application properties are in `src/main/resources/application.properties`.
- Profiles for development and testing are available (`application-dev.properties`, `application-test.properties`).

## API Endpoints

The system exposes RESTful endpoints for managing parking lots, vehicles, tickets, payments, and maintenance. See the controller classes for details.

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Open a pull request.


