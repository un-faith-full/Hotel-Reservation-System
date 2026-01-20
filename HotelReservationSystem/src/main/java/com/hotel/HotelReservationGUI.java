package com.hotel;

import com.hotel.model.*;
import com.hotel.service.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class HotelReservationGUI extends JFrame {
    // Dark Theme Colors
    private static final Color DARK_BG = new Color(33, 37, 41);
    private static final Color DARKER_BG = new Color(23, 26, 30);
    private static final Color ACCENT_COLOR = new Color(0, 150, 220);
    private static final Color ACCENT_HOVER = new Color(0, 180, 255);
    private static final Color TEXT_COLOR = new Color(230, 230, 230);
    private static final Color SECONDARY_TEXT = new Color(180, 180, 180);
    private static final Color TABLE_ALT = new Color(45, 50, 55);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    
    private Hotel hotel;
    private BookingService bookingService;
    private PaymentService paymentService;
    private List<Customer> customers = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    private int customerCount = 1;
    private int bookingCount = 1;
    private int paymentCount = 1;

    private JTabbedPane tabbedPane;
    private JTable customersTable;
    private JTable bookingsTable;
    private DefaultTableModel customersModel;
    private DefaultTableModel bookingsModel;
    private JPanel hotelInfoPanel;

    public HotelReservationGUI() {
        setTitle("Hotel Reservation System");
        setSize(1100, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        
        setDarkTheme();
        initialize();
        setupUI();
    }
    
    private void setDarkTheme() {
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("Panel.foreground", TEXT_COLOR);
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("TextField.background", DARKER_BG);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("TextField.caretForeground", TEXT_COLOR);
        UIManager.put("ComboBox.background", DARKER_BG);
        UIManager.put("ComboBox.foreground", TEXT_COLOR);
        UIManager.put("Table.background", DARKER_BG);
        UIManager.put("Table.foreground", TEXT_COLOR);
        UIManager.put("Table.gridColor", new Color(60, 63, 65));
        UIManager.put("TableHeader.background", new Color(40, 43, 47));
        UIManager.put("TableHeader.foreground", ACCENT_COLOR);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 12));
        UIManager.put("TabbedPane.background", DARK_BG);
        UIManager.put("TabbedPane.foreground", TEXT_COLOR);
        UIManager.put("Label.foreground", TEXT_COLOR);
    }

    private void initialize() {
        hotel = new Hotel("HOTEL001", "ROSE CONTINENTAL", "New York");
        bookingService = new BookingService();
        paymentService = new PaymentService();

        // Add default rooms
        hotel.addRoom(new Room("101", RoomType.SINGLE, 100.0));
        hotel.addRoom(new Room("102", RoomType.SINGLE, 100.0));
        hotel.addRoom(new Room("103", RoomType.DOUBLE, 150.0));
        hotel.addRoom(new Room("104", RoomType.DOUBLE, 150.0));
        hotel.addRoom(new Room("105", RoomType.SUITE, 300.0));
    }

    private void setupUI() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(DARK_BG);
        tabbedPane.setForeground(TEXT_COLOR);

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Customers", createCustomersPanel());
        tabbedPane.addTab("Bookings", createBookingsPanel());
        tabbedPane.addTab("Available Rooms", createAvailableRoomsPanel());
        tabbedPane.addTab("Manage Rooms", createManageRoomsPanel());
        hotelInfoPanel = createHotelInfoPanel();
        tabbedPane.addTab("Hotel Info", hotelInfoPanel);

        add(tabbedPane);
        getContentPane().setBackground(DARK_BG);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        JLabel titleLabel = new JLabel("Hotel Reservation System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(DARK_BG);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Create stat cards with refresh capability
        JPanel customersCard = createStatCard("Total Customers", String.valueOf(customers.size()));
        JPanel bookingsCard = createStatCard("Total Bookings", String.valueOf(bookings.size()));
        JPanel roomsCard = createStatCard("Total Rooms", String.valueOf(hotel.getRooms().size()));
        JPanel hotelCard = createStatCard("Hotel", hotel.getName());
        
        statsPanel.add(customersCard);
        statsPanel.add(bookingsCard);
        statsPanel.add(roomsCard);
        statsPanel.add(hotelCard);

        JButton refreshButton = createStyledButton("Refresh Stats");
        JPanel refreshPanel = new JPanel();
        refreshPanel.setBackground(DARK_BG);
        refreshPanel.add(refreshButton);
        
        refreshButton.addActionListener(e -> {
            // Recreate dashboard
            ((JPanel) tabbedPane.getComponentAt(0)).removeAll();
            JPanel newDash = createDashboardPanel();
            tabbedPane.setComponentAt(0, newDash);
            tabbedPane.repaint();
        });

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(statsPanel, BorderLayout.CENTER);
        panel.add(refreshPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout(10, 5));
        card.setBackground(TABLE_ALT);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(SECONDARY_TEXT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(ACCENT_COLOR);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = createFormPanel("Add New Customer");
        
        formPanel.add(createLabel("Customer Name:"));
        JTextField nameField = createStyledTextField();
        formPanel.add(nameField);
        
        formPanel.add(createLabel("Email:"));
        JTextField emailField = createStyledTextField();
        formPanel.add(emailField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(DARK_BG);
        
        JButton addButton = createStyledButton("Add Customer");
        JButton refreshButton = createStyledButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        // Input Panel with form and buttons
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(DARK_BG);
        inputPanel.add(formPanel, BorderLayout.NORTH);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        String[] columns = {"ID", "Name", "Email"};
        customersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customersTable = createStyledTable(customersModel);
        JScrollPane scrollPane = new JScrollPane(customersTable);
        scrollPane.setBackground(DARKER_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                showErrorDialog("Please fill all fields");
                return;
            }

            String customerId = "CUST" + String.format("%03d", customerCount++);
            Customer customer = new Customer(customerId, name, email);
            customers.add(customer);

            customersModel.addRow(new Object[]{customerId, name, email});
            nameField.setText("");
            emailField.setText("");

            showSuccessDialog("Customer added successfully!");
        });

        refreshButton.addActionListener(e -> {
            customersModel.setRowCount(0);
            for (Customer c : customers) {
                customersModel.addRow(new Object[]{c.getCustomerId(), c.getName(), c.getEmail()});
            }
        });

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel for inputs
        JPanel formPanel = createFormPanel("Create New Booking");

        formPanel.add(createLabel("Select Customer:"));
        JComboBox<String> customerCombo = createStyledCombo();
        formPanel.add(customerCombo);

        formPanel.add(createLabel("Room Type:"));
        JComboBox<String> roomTypeCombo = createStyledCombo(new String[]{"SINGLE", "DOUBLE", "SUITE"});
        formPanel.add(roomTypeCombo);

        formPanel.add(createLabel("Check-in Date (dd-MM-yyyy):"));
        JTextField checkInDateField = createStyledTextField();
        checkInDateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        formPanel.add(checkInDateField);

        formPanel.add(createLabel("Check-in Time (HH:mm):"));
        JTextField checkInTimeField = createStyledTextField();
        checkInTimeField.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        formPanel.add(checkInTimeField);

        formPanel.add(createLabel("Check-out Date (dd-MM-yyyy):"));
        JTextField checkOutDateField = createStyledTextField();
        formPanel.add(checkOutDateField);

        formPanel.add(createLabel("Check-out Time (Auto: 11:00):"));
        JTextField checkOutTimeField = createStyledTextField();
        checkOutTimeField.setText("11:00");
        checkOutTimeField.setEditable(false);
        formPanel.add(checkOutTimeField);

        // Button Panel - buttons in same line
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(DARK_BG);
        
        JButton bookButton = createStyledButton("Create Booking");
        JButton refreshButton = createStyledButton("Refresh");
        
        buttonPanel.add(bookButton);
        buttonPanel.add(refreshButton);

        // Input Panel with form and buttons
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(DARK_BG);
        inputPanel.add(formPanel, BorderLayout.NORTH);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        String[] columns = {"Booking ID", "Customer", "Room", "Check-in", "Check-out", "Total", "Status"};
        bookingsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingsTable = createStyledTable(bookingsModel);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBackground(DARKER_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);

        Runnable updateCombo = () -> {
            customerCombo.removeAllItems();
            for (Customer c : customers) {
                customerCombo.addItem(c.getCustomerId() + " - " + c.getName());
            }
        };

        bookButton.addActionListener(e -> {
            if (customers.isEmpty()) {
                showErrorDialog("No customers available. Add a customer first.");
                return;
            }

            if (customerCombo.getSelectedIndex() < 0) {
                showErrorDialog("Select a customer");
                return;
            }

            Customer selectedCustomer = customers.get(customerCombo.getSelectedIndex());
            String roomType = (String) roomTypeCombo.getSelectedItem();
            String checkInDateStr = checkInDateField.getText().trim();
            String checkInTimeStr = checkInTimeField.getText().trim();
            String checkOutDateStr = checkOutDateField.getText().trim();
            String checkOutTimeStr = checkOutTimeField.getText().trim();

            if (checkInDateStr.isEmpty() || checkInTimeStr.isEmpty() || checkOutDateStr.isEmpty()) {
                showErrorDialog("Please fill all required fields");
                return;
            }

            try {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                
                LocalDate checkInDate = LocalDate.parse(checkInDateStr, dateFormatter);
                LocalDate checkOutDate = LocalDate.parse(checkOutDateStr, dateFormatter);
                LocalTime checkInTime = LocalTime.parse(checkInTimeStr, timeFormatter);
                LocalTime checkOutTime = LocalTime.parse(checkOutTimeStr, timeFormatter);

                if (!checkOutDate.isAfter(checkInDate)) {
                    showErrorDialog("Check-out date must be after check-in date");
                    return;
                }

                var availableRooms = hotel.findAvailableRooms(RoomType.valueOf(roomType));
                if (availableRooms.isEmpty()) {
                    showErrorDialog("No " + roomType + " rooms available");
                    return;
                }

                Room selectedRoom = availableRooms.get(0);
                String bookingId = "BOOK" + String.format("%03d", bookingCount++);
                Booking booking = bookingService.createBooking(bookingId, selectedCustomer, selectedRoom, checkInDate, checkOutDate);
                bookings.add(booking);

                double totalPrice = booking.calculateTotalPrice();

                String paymentId = "PAY" + String.format("%03d", paymentCount++);
                Payment payment = paymentService.createPayment(paymentId, booking, totalPrice);
                paymentService.processPayment(payment);

                String checkInDisplay = checkInDate.format(dateFormatter) + " " + checkInTime.format(timeFormatter);
                String checkOutDisplay = checkOutDate.format(dateFormatter) + " " + checkOutTime.format(timeFormatter);

                bookingsModel.addRow(new Object[]{
                    bookingId,
                    selectedCustomer.getName(),
                    selectedRoom.getRoomNumber() + " (" + roomType + ")",
                    checkInDisplay,
                    checkOutDisplay,
                    "$" + String.format("%.2f", totalPrice),
                    booking.getStatus()
                });

                checkInDateField.setText(LocalDate.now().format(dateFormatter));
                checkInTimeField.setText(LocalTime.now().format(timeFormatter));
                checkOutDateField.setText("");
                checkOutTimeField.setText("11:00");

                showSuccessDialog("Booking created and payment processed!");
            } catch (Exception ex) {
                showErrorDialog("Invalid input: " + ex.getMessage());
            }
        });

        refreshButton.addActionListener(e -> {
            updateCombo.run();
            bookingsModel.setRowCount(0);
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            for (Booking b : bookings) {
                bookingsModel.addRow(new Object[]{
                    b.getBookingId(),
                    b.getCustomer().getName(),
                    b.getRoom().getRoomNumber() + " (" + b.getRoom().getType() + ")",
                    b.getCheckInDate().format(dateFormatter) + " (Check-in time available)",
                    b.getCheckOutDate().format(dateFormatter) + " 11:00",
                    "$" + String.format("%.2f", b.calculateTotalPrice()),
                    b.getStatus()
                });
            }
        });

        updateCombo.run();

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAvailableRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel();
        filterPanel.setBackground(DARK_BG);
        filterPanel.add(createLabel("Room Type:"));
        JComboBox<String> roomTypeCombo = createStyledCombo(new String[]{"ALL", "SINGLE", "DOUBLE", "SUITE"});
        filterPanel.add(roomTypeCombo);

        JButton filterButton = createStyledButton("Filter");
        filterPanel.add(filterButton);

        String[] columns = {"Room Number", "Type", "Price/Night", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(DARKER_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);

        filterButton.addActionListener(e -> {
            String roomType = (String) roomTypeCombo.getSelectedItem();
            model.setRowCount(0);
            
            if ("ALL".equals(roomType)) {
                for (Room r : hotel.getRooms()) {
                    model.addRow(new Object[]{r.getRoomNumber(), r.getType(), "$" + r.getPricePerNight(), "Available"});
                }
            } else {
                var availableRooms = hotel.findAvailableRooms(RoomType.valueOf(roomType));
                for (Room r : availableRooms) {
                    model.addRow(new Object[]{r.getRoomNumber(), r.getType(), "$" + r.getPricePerNight(), "Available"});
                }
            }
        });

        filterButton.doClick();

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createManageRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = createFormPanel("Add New Room - Auto Room Number Assignment");

        formPanel.add(createLabel("Room Type:"));
        JComboBox<String> roomTypeCombo = createStyledCombo(new String[]{"SINGLE", "DOUBLE", "SUITE"});
        formPanel.add(roomTypeCombo);

        formPanel.add(createLabel("Price per Night:"));
        JTextField priceField = createStyledTextField();
        formPanel.add(priceField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(DARK_BG);
        
        JButton addButton = createStyledButton("Add Room (Auto Assigned)");
        JButton refreshButton = createStyledButton("Refresh");
        
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        // Input Panel with form and buttons
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(DARK_BG);
        inputPanel.add(formPanel, BorderLayout.NORTH);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        String[] columns = {"Room Number", "Type", "Price/Night", "Action"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Only action column editable
            }
        };
        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(DARKER_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);

        // Generate next room number based on type
        java.util.function.Function<RoomType, String> getNextRoomNumber = (type) -> {
            int prefix = type == RoomType.SINGLE ? 100 : (type == RoomType.DOUBLE ? 200 : 300);
            int maxNum = prefix;
            for (Room r : hotel.getRooms()) {
                if (r.getType() == type) {
                    try {
                        int num = Integer.parseInt(r.getRoomNumber());
                        if (num > maxNum) maxNum = num;
                    } catch (NumberFormatException ignored) {}
                }
            }
            return String.valueOf(maxNum + 1);
        };

        addButton.addActionListener(e -> {
            String roomType = (String) roomTypeCombo.getSelectedItem();
            String priceStr = priceField.getText().trim();

            if (priceStr.isEmpty()) {
                showErrorDialog("Please enter price");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    showErrorDialog("Price must be greater than 0");
                    return;
                }
                
                String roomNumber = getNextRoomNumber.apply(RoomType.valueOf(roomType));
                Room newRoom = new Room(roomNumber, RoomType.valueOf(roomType), price);
                hotel.addRoom(newRoom);

                priceField.setText("");

                showSuccessDialog("Room " + roomNumber + " added successfully!");
                refreshButton.doClick();
                
                // Refresh dashboard and hotel info
                tabbedPane.setComponentAt(0, createDashboardPanel());
                updateHotelInfoPanel();
            } catch (NumberFormatException ex) {
                showErrorDialog("Invalid price format");
            }
        });

        refreshButton.addActionListener(e -> {
            model.setRowCount(0);
            for (Room r : hotel.getRooms()) {
                model.addRow(new Object[]{r.getRoomNumber(), r.getType(), "$" + r.getPricePerNight(), "Edit"});
            }
        });

        // Add mouse listener for edit action
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (col == 3 && row >= 0) {
                    editRoom(row, model, refreshButton);
                }
            }
        });

        refreshButton.doClick();

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void editRoom(int rowIndex, DefaultTableModel model, JButton refreshButton) {
        String roomNumber = (String) model.getValueAt(rowIndex, 0);
        String currentType = (String) model.getValueAt(rowIndex, 1);
        String currentPrice = ((String) model.getValueAt(rowIndex, 2)).replace("$", "");

        JPanel editPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        editPanel.setBackground(DARK_BG);

        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setForeground(TEXT_COLOR);
        JComboBox<String> typeCombo = createStyledCombo(new String[]{"SINGLE", "DOUBLE", "SUITE"});
        typeCombo.setSelectedItem(currentType);

        JLabel priceLabel = new JLabel("Price per Night:");
        priceLabel.setForeground(TEXT_COLOR);
        JTextField priceField = createStyledTextField();
        priceField.setText(currentPrice);

        editPanel.add(typeLabel);
        editPanel.add(typeCombo);
        editPanel.add(priceLabel);
        editPanel.add(priceField);

        int result = JOptionPane.showConfirmDialog(
            this,
            editPanel,
            "Edit Room " + roomNumber,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                String newType = (String) typeCombo.getSelectedItem();
                double newPrice = Double.parseDouble(priceField.getText());

                if (newPrice <= 0) {
                    showErrorDialog("Price must be greater than 0");
                    return;
                }

                // Find and update the room
                for (Room r : hotel.getRooms()) {
                    if (r.getRoomNumber().equals(roomNumber)) {
                        r.setType(RoomType.valueOf(newType));
                        r.setPricePerNight(newPrice);
                        break;
                    }
                }

                showSuccessDialog("Room updated successfully!");
                refreshButton.doClick();
                updateHotelInfoPanel();
            } catch (NumberFormatException ex) {
                showErrorDialog("Invalid price format");
            }
        }
    }

    private void updateHotelInfoPanel() {
        tabbedPane.setComponentAt(5, createHotelInfoPanel());
        hotelInfoPanel = (JPanel) tabbedPane.getComponentAt(5);
        tabbedPane.repaint();
    }

    private JPanel createHotelInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        infoPanel.setBackground(DARK_BG);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                "Hotel Details",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                ACCENT_COLOR
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        addInfoRow(infoPanel, "Hotel ID:", hotel.getHotelId());
        addInfoRow(infoPanel, "Hotel Name:", hotel.getName());
        addInfoRow(infoPanel, "Location:", hotel.getLocation());
        addInfoRow(infoPanel, "Total Rooms:", String.valueOf(hotel.getRooms().size()));

        long single = hotel.getRooms().stream().filter(r -> r.getType() == RoomType.SINGLE).count();
        long double_rooms = hotel.getRooms().stream().filter(r -> r.getType() == RoomType.DOUBLE).count();
        long suite = hotel.getRooms().stream().filter(r -> r.getType() == RoomType.SUITE).count();

        addInfoRow(infoPanel, "SINGLE Rooms:", String.valueOf(single));
        addInfoRow(infoPanel, "DOUBLE/SUITE Rooms:", String.valueOf(double_rooms + " / " + suite));

        panel.add(infoPanel, BorderLayout.NORTH);

        return panel;
    }

    // Helper Methods
    private JPanel createFormPanel(String title) {
        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 8));
        panel.setBackground(DARK_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                title,
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 13),
                ACCENT_COLOR
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(DARKER_BG);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(TEXT_COLOR);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
        return button;
    }

    private JComboBox<String> createStyledCombo() {
        JComboBox<String> combo = new JComboBox<>();
        combo.setBackground(DARKER_BG);
        combo.setForeground(TEXT_COLOR);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return combo;
    }

    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(DARKER_BG);
        combo.setForeground(TEXT_COLOR);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return combo;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(DARKER_BG);
        table.setForeground(TEXT_COLOR);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(25);
        table.setGridColor(new Color(60, 63, 65));
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(40, 43, 47));
        header.setForeground(ACCENT_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        return table;
    }

    private void addInfoRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelComponent.setForeground(ACCENT_COLOR);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        valueComponent.setForeground(TEXT_COLOR);

        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Success",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelReservationGUI frame = new HotelReservationGUI();
            frame.setVisible(true);
        });
    }
}
