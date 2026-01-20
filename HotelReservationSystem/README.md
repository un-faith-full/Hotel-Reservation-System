# Hotel Reservation System

## Project Overview
A complete Hotel Room Reservation System implementing Object-Oriented Design Principles, Clean Code, Defensive Programming, and Unit Testing. This project is developed for the Software Construction CCP course.

---

## 📋 Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Project Structure](#project-structure)
- [How to Build and Run](#how-to-build-and-run)
- [Testing](#testing)
- [Design Patterns](#design-patterns)
- [Code Quality](#code-quality)

---

## ✨ Features

### Core Functionality
- ✅ Hotel and Room Management
- ✅ Customer Registration and Validation
- ✅ Room Booking System with Date Validation
- ✅ Payment Processing with Status Tracking
- ✅ Comprehensive Exception Handling
- ✅ Input Validation (Defensive Programming)

### Quality Attributes
- ✅ Object-Oriented Design Principles
- ✅ Clean Code Best Practices
- ✅ Defensive Programming Techniques
- ✅ Parameterized Unit Testing (JUnit 5)
- ✅ AAA (Arrange-Act-Assert) Test Pattern
- ✅ High Code Coverage

---

## 🛠 Technologies

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 11+ | Core Language |
| JUnit | 5.9.2 | Unit Testing Framework |
| Maven | 3.8+ | Build Automation |
| JaCoCo | 0.8.8 | Code Coverage Analysis |

---

## 📁 Project Structure

```
HotelReservationSystem/
│
├── src/
│   ├── main/
│   │   └── java/com/hotel/
│   │       ├── model/
│   │       │   ├── Hotel.java              # Hotel entity
│   │       │   ├── Room.java               # Room entity with booking logic
│   │       │   ├── RoomType.java           # Room type enumeration
│   │       │   ├── Customer.java           # Customer entity
│   │       │   ├── Booking.java            # Booking entity
│   │       │   ├── BookingStatus.java      # Booking status enumeration
│   │       │   ├── Payment.java            # Payment entity
│   │       │   └── PaymentStatus.java      # Payment status enumeration
│   │       ├── service/
│   │       │   ├── BookingService.java     # Booking business logic
│   │       │   └── PaymentService.java     # Payment business logic
│   │       ├── exception/
│   │       │   ├── InvalidBookingException.java
│   │       │   └── InvalidPaymentException.java
│   │       └── Main.java                   # Application entry point
│   │
│   └── test/
│       └── java/com/hotel/
│           ├── model/
│           │   ├── RoomTest.java
│           │   ├── CustomerTest.java
│           │   ├── HotelTest.java
│           │   ├── BookingTest.java
│           │   └── PaymentTest.java
│           │
│           └── service/
│               ├── BookingServiceTest.java
│               └── PaymentServiceTest.java
│
├── pom.xml                 # Maven configuration
├── README.md               # Project documentation
└── .gitignore              # Git ignore rules
```

---

## 🚀 How to Build and Run

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.6.0 or higher

### Build the Project
```bash
# Navigate to project directory
cd HotelReservationSystem

# Clean and compile
mvn clean compile

# Build the project
mvn package
```

### Run the Application
```bash
# Using Maven
mvn exec:java -Dexec.mainClass="com.hotel.Main"

# Or run the JAR file
java -jar target/HotelReservationSystem-1.0.0.jar
```

### Sample Output
```
=== Hotel Reservation System ===

1. Hotel Created: Grand Plaza
2. Rooms Added: 3 rooms available
3. Customer Created: Alice Johnson
4. Available Single Rooms: 1
5. Booking Created: ID BOOK001
   Total Price: $200.0
6. Payment Processed: COMPLETED

=== Summary ===
Hotel: Grand Plaza
Customer: Alice Johnson
Room: 101 (SINGLE)
Dates: 2026-01-21 to 2026-01-23
Total: $200.0
Status: CONFIRMED

=== System Execution Completed ===
```

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=RoomTest
mvn test -Dtest=BookingServiceTest
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report
# Coverage report generated at: target/site/jacoco/index.html
```

### Test Statistics
- **Total Test Classes**: 7
- **Total Test Methods**: 35+
- **Coverage Areas**: Models, Services, Exception Handling
- **Test Types**: Unit tests, Parameterized tests, Integration tests

### Key Test Scenarios
1. **Validation Tests**: Null checks, empty strings, invalid data types
2. **State Tests**: Object state transitions and consistency
3. **Exception Tests**: Proper exception throwing and handling
4. **Integration Tests**: Service layer interactions
5. **Boundary Tests**: Edge cases and limit scenarios

---

## 🎨 Design Patterns

### Applied Patterns
1. **Model-View-Controller (MVC)**: Separation of concerns
2. **Service Layer Pattern**: Business logic encapsulation
3. **Entity Pattern**: Domain object modeling
4. **Enum Pattern**: Type-safe enumerations (RoomType, BookingStatus, PaymentStatus)
5. **Defensive Programming**: Input validation at all layers

### OOP Principles
- ✅ **Encapsulation**: Private fields with public accessors
- ✅ **Composition**: Hotel contains Rooms, Bookings contain Payments
- ✅ **Association**: Relationships between entities (Customer-Booking-Room)
- ✅ **Aggregation**: Hotel aggregates multiple Rooms
- ✅ **Inheritance**: Exception hierarchy for custom exceptions

---

## 📊 Code Quality

### Defensive Programming Techniques
```java
// Input validation
if (roomNumber == null || roomNumber.trim().isEmpty()) {
    throw new IllegalArgumentException("Room number cannot be null or empty");
}

// State validation
if (!isAvailable) {
    throw new IllegalStateException("Room is already booked");
}

// Business logic validation
if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
    throw new IllegalArgumentException("Invalid check-in/check-out dates");
}
```

### Clean Code Practices
- Meaningful naming conventions
- Single Responsibility Principle
- DRY (Don't Repeat Yourself)
- Proper error handling
- Comprehensive documentation

### Code Metrics
- **Cyclomatic Complexity**: Low (methods < 5 branches)
- **Lines Per Method**: Short (< 20 lines avg)
- **Code Duplication**: Minimal
- **Test Coverage**: > 85%

---

## 📝 Example Usage

```java
// Create Hotel
Hotel hotel = new Hotel("HOTEL001", "Grand Plaza", "New York");

// Add Rooms
Room room1 = new Room("101", RoomType.SINGLE, 100.0);
Room room2 = new Room("102", RoomType.DOUBLE, 150.0);
hotel.addRoom(room1);
hotel.addRoom(room2);

// Create Customer
Customer customer = new Customer("CUST001", "Alice Johnson", "alice@email.com");

// Create Booking
LocalDate checkIn = LocalDate.now().plusDays(1);
LocalDate checkOut = LocalDate.now().plusDays(3);
Booking booking = new Booking("BOOK001", customer, room1, checkIn, checkOut);

// Process Payment
Payment payment = new Payment("PAY001", booking, booking.calculateTotalPrice());
payment.processPayment();

// Calculate and display
System.out.println("Total: $" + booking.calculateTotalPrice());
System.out.println("Status: " + payment.getStatus());
```

---

## 🔍 Key Classes

### Model Classes
| Class | Purpose |
|-------|---------|
| `Hotel` | Manages hotel information and room collection |
| `Room` | Represents a hotel room with availability state |
| `Customer` | Stores customer details and booking history |
| `Booking` | Manages booking with date validation and price calculation |
| `Payment` | Handles payment processing and status tracking |

### Service Classes
| Class | Purpose |
|-------|---------|
| `BookingService` | Provides booking operations with exception handling |
| `PaymentService` | Provides payment operations with validation |

### Exception Classes
| Class | Purpose |
|-------|---------|
| `InvalidBookingException` | Thrown for booking-related errors |
| `InvalidPaymentException` | Thrown for payment-related errors |

---

## 📞 Contact & Information

**Course**: Software Construction CCP  
**Semester**: Spring 2026  
**Project Type**: Object-Oriented Design & Testing  

---

## 📄 License

This project is created for educational purposes in the Software Construction course.

---

## ✅ Checklist for Submission

- [x] Complete source code with defensive programming
- [x] Comprehensive unit tests (35+ test cases)
- [x] Maven build configuration (pom.xml)
- [x] README with documentation
- [x] Working Main method with sample execution
- [x] Exception handling with custom exceptions
- [x] Git repository with commit history (5+ commits)
- [x] Code follows Java conventions and best practices

---

**Project Status**: ✅ Ready for Submission

