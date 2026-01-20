package com.hotel;

import com.hotel.model.*;
import com.hotel.service.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static Hotel hotel;
    private static BookingService bookingService;
    private static PaymentService paymentService;
    private static Scanner scanner;
    private static List<Customer> customers = new ArrayList<>();
    private static List<Booking> bookings = new ArrayList<>();
    private static int customerCount = 1;
    private static int bookingCount = 1;
    private static int paymentCount = 1;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        bookingService = new BookingService();
        paymentService = new PaymentService();

        initializeHotel();

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    listCustomers();
                    break;
                case 3:
                    createBooking();
                    break;
                case 4:
                    viewAvailableRooms();
                    break;
                case 5:
                    viewAllBookings();
                    break;
                case 6:
                    viewHotelInfo();
                    break;
                case 7:
                    running = false;
                    System.out.println("\n=== Thank you for using Hotel Reservation System ===");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void initializeHotel() {
        hotel = new Hotel("HOTEL001", "Grand Plaza", "New York");
        System.out.println("\n=== Hotel Reservation System Initialized ===");
        System.out.println("Hotel: " + hotel.getName() + " in " + hotel.getLocation());

        // Add default rooms
        hotel.addRoom(new Room("101", RoomType.SINGLE, 100.0));
        hotel.addRoom(new Room("102", RoomType.SINGLE, 100.0));
        hotel.addRoom(new Room("103", RoomType.DOUBLE, 150.0));
        hotel.addRoom(new Room("104", RoomType.DOUBLE, 150.0));
        hotel.addRoom(new Room("105", RoomType.SUITE, 300.0));

        System.out.println("5 rooms added to the hotel.\n");
    }

    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Add New Customer");
        System.out.println("2. View All Customers");
        System.out.println("3. Create Booking");
        System.out.println("4. View Available Rooms");
        System.out.println("5. View All Bookings");
        System.out.println("6. View Hotel Information");
        System.out.println("7. Exit");
        System.out.println("==============================");
    }

    private static void addCustomer() {
        System.out.println("\n--- Add New Customer ---");
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        String customerId = "CUST" + String.format("%03d", customerCount++);
        Customer customer = new Customer(customerId, name, email);
        customers.add(customer);

        System.out.println("✓ Customer added successfully!");
        System.out.println("  ID: " + customerId + ", Name: " + name + ", Email: " + email);
    }

    private static void listCustomers() {
        System.out.println("\n--- All Customers ---");
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }

        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            System.out.println((i + 1) + ". ID: " + c.getCustomerId() + " | Name: " + c.getName() + " | Email: " + c.getEmail());
        }
    }

    private static void createBooking() {
        System.out.println("\n--- Create Booking ---");

        if (customers.isEmpty()) {
            System.out.println("No customers available. Please add a customer first.");
            return;
        }

        // Select customer
        listCustomers();
        System.out.print("Select customer number: ");
        int custChoice = getIntInput("") - 1;

        if (custChoice < 0 || custChoice >= customers.size()) {
            System.out.println("Invalid customer selection.");
            return;
        }

        Customer selectedCustomer = customers.get(custChoice);

        // View available rooms
        System.out.print("Enter room type (SINGLE/DOUBLE/SUITE): ");
        String roomTypeInput = scanner.nextLine().toUpperCase();
        RoomType selectedRoomType;

        try {
            selectedRoomType = RoomType.valueOf(roomTypeInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room type.");
            return;
        }

        var availableRooms = hotel.findAvailableRooms(selectedRoomType);
        if (availableRooms.isEmpty()) {
            System.out.println("No " + selectedRoomType + " rooms available.");
            return;
        }

        System.out.println("Available " + selectedRoomType + " rooms:");
        for (int i = 0; i < availableRooms.size(); i++) {
            Room r = availableRooms.get(i);
            System.out.println((i + 1) + ". Room " + r.getRoomNumber() + " - $" + r.getPricePerNight() + "/night");
        }

        System.out.print("Select room number (1-" + availableRooms.size() + "): ");
        int roomChoice = getIntInput("") - 1;

        if (roomChoice < 0 || roomChoice >= availableRooms.size()) {
            System.out.println("Invalid room selection.");
            return;
        }

        Room selectedRoom = availableRooms.get(roomChoice);

        // Get check-in and check-out dates
        System.out.print("Enter check-in date (yyyy-MM-dd): ");
        LocalDate checkIn = parseDate(scanner.nextLine());
        if (checkIn == null) return;

        System.out.print("Enter check-out date (yyyy-MM-dd): ");
        LocalDate checkOut = parseDate(scanner.nextLine());
        if (checkOut == null) return;

        if (!checkOut.isAfter(checkIn)) {
            System.out.println("Check-out date must be after check-in date.");
            return;
        }

        // Create booking
        String bookingId = "BOOK" + String.format("%03d", bookingCount++);
        Booking booking = bookingService.createBooking(bookingId, selectedCustomer, selectedRoom, checkIn, checkOut);
        bookings.add(booking);

        double totalPrice = booking.calculateTotalPrice();
        System.out.println("\n✓ Booking created successfully!");
        System.out.println("  Booking ID: " + bookingId);
        System.out.println("  Customer: " + selectedCustomer.getName());
        System.out.println("  Room: " + selectedRoom.getRoomNumber() + " (" + selectedRoom.getType() + ")");
        System.out.println("  Check-in: " + checkIn);
        System.out.println("  Check-out: " + checkOut);
        System.out.println("  Total Price: $" + totalPrice);

        // Process payment
        System.out.print("\nProcess payment now? (yes/no): ");
        String paymentChoice = scanner.nextLine().toLowerCase();

        if (paymentChoice.equals("yes") || paymentChoice.equals("y")) {
            String paymentId = "PAY" + String.format("%03d", paymentCount++);
            Payment payment = paymentService.createPayment(paymentId, booking, totalPrice);
            paymentService.processPayment(payment);

            System.out.println("✓ Payment processed successfully!");
            System.out.println("  Payment ID: " + paymentId);
            System.out.println("  Amount: $" + totalPrice);
            System.out.println("  Status: " + payment.getStatus());
        }
    }

    private static void viewAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        System.out.print("Enter room type (SINGLE/DOUBLE/SUITE): ");
        String roomTypeInput = scanner.nextLine().toUpperCase();

        RoomType selectedRoomType;
        try {
            selectedRoomType = RoomType.valueOf(roomTypeInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room type.");
            return;
        }

        var availableRooms = hotel.findAvailableRooms(selectedRoomType);

        if (availableRooms.isEmpty()) {
            System.out.println("No " + selectedRoomType + " rooms available.");
            return;
        }

        System.out.println("Available " + selectedRoomType + " rooms:");
        for (Room r : availableRooms) {
            System.out.println("  - Room " + r.getRoomNumber() + " | Price: $" + r.getPricePerNight() + "/night");
        }
    }

    private static void viewAllBookings() {
        System.out.println("\n--- All Bookings ---");
        if (bookings.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Booking b : bookings) {
            System.out.println("\nBooking ID: " + b.getBookingId());
            System.out.println("  Customer: " + b.getCustomer().getName());
            System.out.println("  Room: " + b.getRoom().getRoomNumber() + " (" + b.getRoom().getType() + ")");
            System.out.println("  Check-in: " + b.getCheckInDate());
            System.out.println("  Check-out: " + b.getCheckOutDate());
            System.out.println("  Total Price: $" + b.calculateTotalPrice());
            System.out.println("  Status: " + b.getStatus());
        }
    }

    private static void viewHotelInfo() {
        System.out.println("\n--- Hotel Information ---");
        System.out.println("Hotel ID: " + hotel.getHotelId());
        System.out.println("Hotel Name: " + hotel.getName());
        System.out.println("Location: " + hotel.getLocation());
        System.out.println("Total Rooms: " + hotel.getRooms().size());
        System.out.println("\nRoom Breakdown:");
        long single = hotel.getRooms().stream().filter(r -> r.getType() == RoomType.SINGLE).count();
        long double_rooms = hotel.getRooms().stream().filter(r -> r.getType() == RoomType.DOUBLE).count();
        long suite = hotel.getRooms().stream().filter(r -> r.getType() == RoomType.SUITE).count();
        System.out.println("  - SINGLE: " + single);
        System.out.println("  - DOUBLE: " + double_rooms);
        System.out.println("  - SUITE: " + suite);
    }

    private static int getIntInput(String prompt) {
        try {
            if (!prompt.isEmpty()) {
                System.out.print(prompt);
            }
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private static LocalDate parseDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateString, formatter);
        } catch (Exception e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            return null;
        }
    }
}
