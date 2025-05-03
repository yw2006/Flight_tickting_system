import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {
    // Main panels
    private JPanel mainPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    // Navigation panel components
    private JPanel navPanel;
    private JButton flightsBtn;
    private JButton airportsBtn;
    private JButton airlinesBtn;
    private JButton aircraftBtn;
    private JButton customersBtn;
    
    // Tables for each section
    private JTable flightsTable;
    private JTable airportsTable;
    private JTable airlinesTable;
    private JTable aircraftTable;
    private JTable customersTable;
    
    // Table models
    private DefaultTableModel flightsModel;
    private DefaultTableModel airportsModel;
    private DefaultTableModel airlinesModel;
    private DefaultTableModel aircraftModel;
    private DefaultTableModel customersModel;
    
    public AdminDashboard() {
        // Set up the JFrame
        super("Flight Management System - Admin Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize components
        initComponents();
        
        // Set up the layout
        setupLayout();
        
        // Load dummy data
        loadDummyData();
        
        // Display the frame
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initComponents() {
        // Initialize main panels
        mainPanel = new JPanel(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Initialize navigation buttons
        navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navPanel.setBackground(new Color(48, 63, 159));
        
        flightsBtn = createNavButton("Active Flights");
        airportsBtn = createNavButton("Airports");
        airlinesBtn = createNavButton("Airlines");
        aircraftBtn = createNavButton("Aircraft");
        customersBtn = createNavButton("Customers");
        
        // Initialize table models
        initializeTableModels();
        
        // Initialize tables
        flightsTable = new JTable(flightsModel);
        airportsTable = new JTable(airportsModel);
        airlinesTable = new JTable(airlinesModel);
        aircraftTable = new JTable(aircraftModel);
        customersTable = new JTable(customersModel);
        
        // Add button action listeners
        addActionListeners();
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(48, 63, 159));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(98, 113, 209));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(48, 63, 159));
            }
        });
        
        return button;
    }
    
    private void setupLayout() {
        // Add navigation buttons to navPanel
        navPanel.add(createTitleLabel());
        navPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        navPanel.add(flightsBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(airportsBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(airlinesBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(aircraftBtn);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navPanel.add(customersBtn);
        navPanel.add(Box.createVerticalGlue());
        
        // Set up card panels for each section
        cardPanel.add(createFlightsPanel(), "flights");
        cardPanel.add(createAirportsPanel(), "airports");
        cardPanel.add(createAirlinesPanel(), "airlines");
        cardPanel.add(createAircraftPanel(), "aircraft");
        cardPanel.add(createCustomersPanel(), "customers");
        
        // Add components to main panel
        mainPanel.add(navPanel, BorderLayout.WEST);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        
        // Add main panel to frame
        add(mainPanel);
    }
    
    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return titleLabel;
    }
    
    private void addActionListeners() {
        flightsBtn.addActionListener(_ -> cardLayout.show(cardPanel, "flights"));
        airportsBtn.addActionListener(_ -> cardLayout.show(cardPanel, "airports"));
        airlinesBtn.addActionListener(_ -> cardLayout.show(cardPanel, "airlines"));
        aircraftBtn.addActionListener(_ -> cardLayout.show(cardPanel, "aircraft"));
        customersBtn.addActionListener(_ -> cardLayout.show(cardPanel, "customers"));
    }
    
    private void initializeTableModels() {
        // Flights table model
        flightsModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Flight Number", "Departure", "Arrival", "Duration (min)", "Gate", "Status", "Departure Time"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only status column is editable
            }
        };
        
        // Airports table model
        airportsModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Code", "Name", "City", "State", "Country"}
        );
        
        // Airlines table model
        airlinesModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Code", "Name", "Headquarters"}
        );
        
        // Aircraft table model
        aircraftModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"Name", "Model", "Manufacturing Year", "Capacity"}
        );
        
        // Customers table model
        customersModel = new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Name", "Email", "Phone", "Status", "Actions"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only status column is editable
            }
        };
    }
    
    // Create panel for flights section
    private JPanel createFlightsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header panel with title and add button
        JPanel headerPanel = createHeaderPanel("Active Flights");
        JButton addFlightBtn = new JButton("Add Flight");
        addFlightBtn.addActionListener(_ -> showAddFlightDialog());
        headerPanel.add(addFlightBtn);
        
        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        
        // Create action panel with buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        JButton scheduleBtn = new JButton("Manage Schedule");
        JButton seatsBtn = new JButton("Manage Seats");
        
        editBtn.addActionListener(_ -> editSelectedFlight());
        deleteBtn.addActionListener(_ -> deleteSelectedFlight());
        scheduleBtn.addActionListener(_ -> manageFlightSchedule());
        seatsBtn.addActionListener(_ -> manageFlightSeats());
        
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        actionPanel.add(scheduleBtn);
        actionPanel.add(seatsBtn);
        
        // Add components to panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Create panel for airports section
    private JPanel createAirportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header panel with title and add button
        JPanel headerPanel = createHeaderPanel("Airports");
        JButton addAirportBtn = new JButton("Add Airport");
        addAirportBtn.addActionListener(_ -> showAddAirportDialog());
        headerPanel.add(addAirportBtn);
        
        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(airportsTable);
        
        // Create action panel with buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        
        editBtn.addActionListener(_ -> editSelectedAirport());
        deleteBtn.addActionListener(_ -> deleteSelectedAirport());
        
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        
        // Add components to panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Create panel for airlines section
    private JPanel createAirlinesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header panel with title and add button
        JPanel headerPanel = createHeaderPanel("Airlines");
        JButton addAirlineBtn = new JButton("Add Airline");
        addAirlineBtn.addActionListener(_ -> showAddAirlineDialog());
        headerPanel.add(addAirlineBtn);
        
        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(airlinesTable);
        
        // Create action panel with buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        
        editBtn.addActionListener(_ -> editSelectedAirline());
        deleteBtn.addActionListener(_ -> deleteSelectedAirline());
        
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        
        // Add components to panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Create panel for aircraft section
    private JPanel createAircraftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header panel with title and add button
        JPanel headerPanel = createHeaderPanel("Aircraft");
        JButton addAircraftBtn = new JButton("Add Aircraft");
        addAircraftBtn.addActionListener(_ -> showAddAircraftDialog());
        headerPanel.add(addAircraftBtn);
        
        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(aircraftTable);
        
        // Create action panel with buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton editBtn = new JButton("Edit Selected");
        JButton deleteBtn = new JButton("Delete Selected");
        
        editBtn.addActionListener(_ -> editSelectedAircraft());
        deleteBtn.addActionListener(_ -> deleteSelectedAircraft());
        
        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        
        // Add components to panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Create panel for customers section
    private JPanel createCustomersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header panel with title and search field
        JPanel headerPanel = createHeaderPanel("Customers");
        JTextField searchField = new JTextField(20);
        JButton searchBtn = new JButton("Search");
        headerPanel.add(new JLabel("Search: "));
        headerPanel.add(searchField);
        headerPanel.add(searchBtn);
        
        // Create table panel with scroll pane
        JScrollPane scrollPane = new JScrollPane(customersTable);
        
        // Create action panel with buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton blockBtn = new JButton("Block Selected");
        JButton blacklistBtn = new JButton("Blacklist Selected");
        JButton activateBtn = new JButton("Activate Selected");
        
        blockBtn.addActionListener(_ -> blockSelectedCustomer());
        blacklistBtn.addActionListener(_ -> blacklistSelectedCustomer());
        activateBtn.addActionListener(_ -> activateSelectedCustomer());
        
        actionPanel.add(blockBtn);
        actionPanel.add(blacklistBtn);
        actionPanel.add(activateBtn);
        
        // Add components to panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createHeaderPanel(String title) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);
        return headerPanel;
    }
    
    // CRUD operations and dialog methods
    private void showAddFlightDialog() {
        JDialog dialog = new JDialog(this, "Add New Flight", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Flight Number:"));
        JTextField flightNumberField = new JTextField();
        panel.add(flightNumberField);
        
        panel.add(new JLabel("Departure Airport:"));
        JTextField departureField = new JTextField();
        panel.add(departureField);
        
        panel.add(new JLabel("Arrival Airport:"));
        JTextField arrivalField = new JTextField();
        panel.add(arrivalField);
        
        panel.add(new JLabel("Duration (min):"));
        JTextField durationField = new JTextField();
        panel.add(durationField);
        
        panel.add(new JLabel("Gate:"));
        JTextField gateField = new JTextField();
        panel.add(gateField);
        
        panel.add(new JLabel("Status:"));
        String[] statuses = {"Active", "Delayed", "Landed", "Departed", "Canceled", "Aborted", "Unknown"};
        JComboBox<String> statusCombo = new JComboBox<>(statuses);
        panel.add(statusCombo);
        
        panel.add(new JLabel("Departure Time:"));
        JTextField timeField = new JTextField("HH:MM");
        panel.add(timeField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            flightsModel.addRow(new Object[]{
                flightNumberField.getText(),
                departureField.getText(),
                arrivalField.getText(),
                durationField.getText(),
                gateField.getText(),
                statusCombo.getSelectedItem(),
                timeField.getText()
            });
            dialog.dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editSelectedFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow != -1) {
            JDialog dialog = new JDialog(this, "Edit Flight", true);
            dialog.setSize(400, 400);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            panel.add(new JLabel("Flight Number:"));
            JTextField flightNumberField = new JTextField((String) flightsModel.getValueAt(selectedRow, 0));
            panel.add(flightNumberField);
            
            panel.add(new JLabel("Departure Airport:"));
            JTextField departureField = new JTextField((String) flightsModel.getValueAt(selectedRow, 1));
            panel.add(departureField);
            
            panel.add(new JLabel("Arrival Airport:"));
            JTextField arrivalField = new JTextField((String) flightsModel.getValueAt(selectedRow, 2));
            panel.add(arrivalField);
            
            panel.add(new JLabel("Duration (min):"));
            JTextField durationField = new JTextField(flightsModel.getValueAt(selectedRow, 3).toString());
            panel.add(durationField);
            
            panel.add(new JLabel("Gate:"));
            JTextField gateField = new JTextField((String) flightsModel.getValueAt(selectedRow, 4));
            panel.add(gateField);
            
            panel.add(new JLabel("Status:"));
            String[] statuses = {"Active", "Delayed", "Landed", "Departed", "Canceled", "Aborted", "Unknown"};
            JComboBox<String> statusCombo = new JComboBox<>(statuses);
            statusCombo.setSelectedItem(flightsModel.getValueAt(selectedRow, 5));
            panel.add(statusCombo);
            
            panel.add(new JLabel("Departure Time:"));
            JTextField timeField = new JTextField((String) flightsModel.getValueAt(selectedRow, 6));
            panel.add(timeField);
            
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(_ -> {
                flightsModel.setValueAt(flightNumberField.getText(), selectedRow, 0);
                flightsModel.setValueAt(departureField.getText(), selectedRow, 1);
                flightsModel.setValueAt(arrivalField.getText(), selectedRow, 2);
                flightsModel.setValueAt(durationField.getText(), selectedRow, 3);
                flightsModel.setValueAt(gateField.getText(), selectedRow, 4);
                flightsModel.setValueAt(statusCombo.getSelectedItem(), selectedRow, 5);
                flightsModel.setValueAt(timeField.getText(), selectedRow, 6);
                dialog.dispose();
            });
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(_ -> dialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedFlight() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this flight?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                flightsModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void manageFlightSchedule() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow != -1) {
            String flightNumber = (String) flightsModel.getValueAt(selectedRow, 0);
            JDialog dialog = new JDialog(this, "Manage Schedule for Flight " + flightNumber, true);
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new BorderLayout());
            
            // Create tabs for Weekly and Custom schedules
            JTabbedPane tabs = new JTabbedPane();
            
            // Weekly schedule tab
            JPanel weeklyPanel = new JPanel(new BorderLayout());
            DefaultTableModel weeklyModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"Day of Week", "Departure Time"}
            );
            JTable weeklyTable = new JTable(weeklyModel);
            JScrollPane weeklyScroll = new JScrollPane(weeklyTable);
            
            JPanel weeklyActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addWeeklyBtn = new JButton("Add Schedule");
            JButton removeWeeklyBtn = new JButton("Remove Selected");
            weeklyActionPanel.add(addWeeklyBtn);
            weeklyActionPanel.add(removeWeeklyBtn);
            
            weeklyPanel.add(weeklyScroll, BorderLayout.CENTER);
            weeklyPanel.add(weeklyActionPanel, BorderLayout.SOUTH);
            
            // Custom schedule tab
            JPanel customPanel = new JPanel(new BorderLayout());
            DefaultTableModel customModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"Date", "Departure Time"}
            );
            JTable customTable = new JTable(customModel);
            JScrollPane customScroll = new JScrollPane(customTable);
            
            JPanel customActionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addCustomBtn = new JButton("Add Schedule");
            JButton removeCustomBtn = new JButton("Remove Selected");
            customActionPanel.add(addCustomBtn);
            customActionPanel.add(removeCustomBtn);
            
            customPanel.add(customScroll, BorderLayout.CENTER);
            customPanel.add(customActionPanel, BorderLayout.SOUTH);
            
            // Add tabs to tabbed pane
            tabs.addTab("Weekly Schedule", weeklyPanel);
            tabs.addTab("Custom Schedule", customPanel);
            
            // Add action listeners
            addWeeklyBtn.addActionListener(_ -> {
                // Show dialog to add weekly schedule
                JDialog addDialog = new JDialog(dialog, "Add Weekly Schedule", true);
                addDialog.setSize(300, 150);
                addDialog.setLocationRelativeTo(dialog);
                
                JPanel addPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                addPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                JComboBox<String> dayCombo = new JComboBox<>(days);
                JTextField timeField = new JTextField("HH:MM");
                
                addPanel.add(new JLabel("Day:"));
                addPanel.add(dayCombo);
                addPanel.add(new JLabel("Time:"));
                addPanel.add(timeField);
                
                JButton saveBtn = new JButton("Save");
                JButton cancelBtn = new JButton("Cancel");
                
                saveBtn.addActionListener(event -> {
                    weeklyModel.addRow(new Object[]{
                        dayCombo.getSelectedItem(),
                        timeField.getText()
                    });
                    addDialog.dispose();
                });
                
                cancelBtn.addActionListener(event -> addDialog.dispose());
                
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.add(saveBtn);
                buttonPanel.add(cancelBtn);
                
                addDialog.setLayout(new BorderLayout());
                addDialog.add(addPanel, BorderLayout.CENTER);
                addDialog.add(buttonPanel, BorderLayout.SOUTH);
                addDialog.setVisible(true);
            });
            
            removeWeeklyBtn.addActionListener(_ -> {
                int row = weeklyTable.getSelectedRow();
                if (row != -1) {
                    weeklyModel.removeRow(row);
                }
            });
            
            addCustomBtn.addActionListener(_ -> {
                // Show dialog to add custom schedule
                JDialog addDialog = new JDialog(dialog, "Add Custom Schedule", true);
                addDialog.setSize(300, 150);
                addDialog.setLocationRelativeTo(dialog);
                
                JPanel addPanel = new JPanel(new GridLayout(3, 2, 10, 10));
                addPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                JTextField dateField = new JTextField("YYYY-MM-DD");
                JTextField timeField = new JTextField("HH:MM");
                
                addPanel.add(new JLabel("Date:"));
                addPanel.add(dateField);
                addPanel.add(new JLabel("Time:"));
                addPanel.add(timeField);
                
                JButton saveBtn = new JButton("Save");
                JButton cancelBtn = new JButton("Cancel");
                
                saveBtn.addActionListener(event -> {
                    customModel.addRow(new Object[]{
                        dateField.getText(),
                        timeField.getText()
                    });
                    addDialog.dispose();
                });
                
                cancelBtn.addActionListener(event -> addDialog.dispose());
                
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.add(saveBtn);
                buttonPanel.add(cancelBtn);
                
                addDialog.setLayout(new BorderLayout());
                addDialog.add(addPanel, BorderLayout.CENTER);
                addDialog.add(buttonPanel, BorderLayout.SOUTH);
                addDialog.setVisible(true);
            });
            
            removeCustomBtn.addActionListener(_ -> {
                int row = customTable.getSelectedRow();
                if (row != -1) {
                    customModel.removeRow(row);
                }
            });
            
            // Add to main panel
            panel.add(tabs, BorderLayout.CENTER);
            
            // Close button
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(_ -> dialog.dispose());
            
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.add(closeButton);
            panel.add(bottomPanel, BorderLayout.SOUTH);
            
            dialog.add(panel);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight to manage its schedule.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void manageFlightSeats() {
        int selectedRow = flightsTable.getSelectedRow();
        if (selectedRow != -1) {
            String flightNumber = (String) flightsModel.getValueAt(selectedRow, 0);
            JDialog dialog = new JDialog(this, "Manage Seats for Flight " + flightNumber, true);
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new BorderLayout());
            
            // Create a table for seats
            DefaultTableModel seatModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"Seat Number", "Type", "Class", "Fare", "Reservation Status"}
            );
            JTable seatTable = new JTable(seatModel);
            JScrollPane scrollPane = new JScrollPane(seatTable);
            
            // Add some dummy data
            seatModel.addRow(new Object[]{"1A", "Regular", "FirstClass", "$350", "Available"});
            seatModel.addRow(new Object[]{"1B", "Regular", "FirstClass", "$350", "Reserved"});
            seatModel.addRow(new Object[]{"2A", "Regular", "Business", "$250", "Available"});
            seatModel.addRow(new Object[]{"10C", "EmergencyExit", "Economy", "$150", "Available"});
            
            // Action panel with buttons
            JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton addBtn = new JButton("Add Seat");
            JButton editBtn = new JButton("Edit Selected");
            JButton deleteBtn = new JButton("Delete Selected");
            
            actionPanel.add(addBtn);
            actionPanel.add(editBtn);
            actionPanel.add(deleteBtn);
            
            // Add button actions
            addBtn.addActionListener(_ -> {
                JDialog addDialog = new JDialog(dialog, "Add Seat", true);
                addDialog.setSize(350, 250);
                addDialog.setLocationRelativeTo(dialog);
                
                JPanel addPanel = new JPanel(new GridLayout(6, 2, 10, 10));
                addPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                JTextField seatNumberField = new JTextField();
                
                String[] types = {"Regular", "Accessible", "EmergencyExit", "ExtraLegRoom"};
                JComboBox<String> typeCombo = new JComboBox<>(types);
                
                String[] classes = {"Economy", "PremiumEconomy", "Business", "FirstClass"};
                JComboBox<String> classCombo = new JComboBox<>(classes);
                
                JTextField fareField = new JTextField();
                
                addPanel.add(new JLabel("Seat Number:"));
                addPanel.add(seatNumberField);
                addPanel.add(new JLabel("Type:"));
                addPanel.add(typeCombo);
                addPanel.add(new JLabel("Class:"));
                addPanel.add(classCombo);
                addPanel.add(new JLabel("Fare:"));
                addPanel.add(fareField);
                
                JButton saveBtn = new JButton("Save");
                JButton cancelBtn = new JButton("Cancel");
                
                saveBtn.addActionListener(event -> {
                    seatModel.addRow(new Object[]{
                        seatNumberField.getText(),
                        typeCombo.getSelectedItem(),
                        classCombo.getSelectedItem(),
                        "$" + fareField.getText(),
                        "Available"
                    });
                    addDialog.dispose();
                });
                
                cancelBtn.addActionListener(event -> addDialog.dispose());
                
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.add(saveBtn);
                buttonPanel.add(cancelBtn);
                
                addDialog.setLayout(new BorderLayout());
                addDialog.add(addPanel, BorderLayout.CENTER);
                addDialog.add(buttonPanel, BorderLayout.SOUTH);
                addDialog.setVisible(true);
            });
            
            editBtn.addActionListener(_ -> {
                int row = seatTable.getSelectedRow();
                if (row != -1) {
                    JDialog editDialog = new JDialog(dialog, "Edit Seat", true);
                    editDialog.setSize(350, 250);
                    editDialog.setLocationRelativeTo(dialog);
                    
                    JPanel editPanel = new JPanel(new GridLayout(6, 2, 10, 10));
                    editPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    
                    JTextField seatNumberField = new JTextField((String) seatModel.getValueAt(row, 0));
                    
                    String[] types = {"Regular", "Accessible", "EmergencyExit", "ExtraLegRoom"};
                    JComboBox<String> typeCombo = new JComboBox<>(types);
                    typeCombo.setSelectedItem(seatModel.getValueAt(row, 1));
                    
                    String[] classes = {"Economy", "PremiumEconomy", "Business", "FirstClass"};
                    JComboBox<String> classCombo = new JComboBox<>(classes);
                    classCombo.setSelectedItem(seatModel.getValueAt(row, 2));
                    
                    String fare = ((String) seatModel.getValueAt(row, 3)).replace("$", "");
                    JTextField fareField = new JTextField(fare);
                    
                    String[] statuses = {"Available", "Reserved"};
                    JComboBox<String> statusCombo = new JComboBox<>(statuses);
                    statusCombo.setSelectedItem(seatModel.getValueAt(row, 4));
                    
                    editPanel.add(new JLabel("Seat Number:"));
                    editPanel.add(seatNumberField);
                    editPanel.add(new JLabel("Type:"));
                    editPanel.add(typeCombo);
                    editPanel.add(new JLabel("Class:"));
                    editPanel.add(classCombo);
                    editPanel.add(new JLabel("Fare:"));
                    editPanel.add(fareField);
                    editPanel.add(new JLabel("Status:"));
                    editPanel.add(statusCombo);
                    
                    JButton saveBtn = new JButton("Save");
                    JButton cancelBtn = new JButton("Cancel");
                    
                    saveBtn.addActionListener(event -> {
                        seatModel.setValueAt(seatNumberField.getText(), row, 0);
                        seatModel.setValueAt(typeCombo.getSelectedItem(), row, 1);
                        seatModel.setValueAt(classCombo.getSelectedItem(), row, 2);
                        seatModel.setValueAt("$" + fareField.getText(), row, 3);
                        seatModel.setValueAt(statusCombo.getSelectedItem(), row, 4);
                        editDialog.dispose();
                    });
                    
                    cancelBtn.addActionListener(event -> editDialog.dispose());
                    
                    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    buttonPanel.add(saveBtn);
                    buttonPanel.add(cancelBtn);
                    
                    editDialog.setLayout(new BorderLayout());
                    editDialog.add(editPanel, BorderLayout.CENTER);
                    editDialog.add(buttonPanel, BorderLayout.SOUTH);
                    editDialog.setVisible(true);
                }
            });
            
            deleteBtn.addActionListener(_ -> {
                int row = seatTable.getSelectedRow();
                if (row != -1) {
                    int confirm = JOptionPane.showConfirmDialog(dialog, 
                        "Are you sure you want to delete this seat?", 
                        "Confirm Deletion", 
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        seatModel.removeRow(row);
                    }
                }
            });
            
            // Add a visual seat map panel
            JPanel seatMapPanel = new JPanel();
            seatMapPanel.setBorder(BorderFactory.createTitledBorder("Seat Map"));
            seatMapPanel.setPreferredSize(new Dimension(0, 150));
            
            // Add a simple grid layout to show seat map
            seatMapPanel.setLayout(new GridLayout(4, 6, 5, 5));
            
            // Add dummy seat buttons
            for (int i = 1; i <= 4; i++) {
                for (char c = 'A'; c <= 'F'; c++) {
                    JButton seatBtn = new JButton(i + "" + c);
                    if ((i == 1 && c == 'B') || (i == 2 && c == 'E')) {
                        seatBtn.setBackground(Color.RED);
                        seatBtn.setToolTipText("Reserved");
                    } else {
                        seatBtn.setBackground(Color.GREEN);
                        seatBtn.setToolTipText("Available");
                    }
                    seatMapPanel.add(seatBtn);
                }
            }
            
            // Add to main panel
            panel.add(scrollPane, BorderLayout.CENTER);
            panel.add(actionPanel, BorderLayout.SOUTH);
            panel.add(seatMapPanel, BorderLayout.NORTH);
            
            // Close button
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(_ -> dialog.dispose());
            
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.add(closeButton);
            panel.add(bottomPanel, BorderLayout.SOUTH);
            
            dialog.add(panel);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a flight to manage its seats.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showAddAirportDialog() {
        JDialog dialog = new JDialog(this, "Add New Airport", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Code:"));
        JTextField codeField = new JTextField();
        panel.add(codeField);
        
        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);
        
        panel.add(new JLabel("City:"));
        JTextField cityField = new JTextField();
        panel.add(cityField);
        
        panel.add(new JLabel("State:"));
        JTextField stateField = new JTextField();
        panel.add(stateField);
        
        panel.add(new JLabel("Country:"));
        JTextField countryField = new JTextField();
        panel.add(countryField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            airportsModel.addRow(new Object[]{
                codeField.getText(),
                nameField.getText(),
                cityField.getText(),
                stateField.getText(),
                countryField.getText()
            });
            dialog.dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editSelectedAirport() {
        int selectedRow = airportsTable.getSelectedRow();
        if (selectedRow != -1) {
            JDialog dialog = new JDialog(this, "Edit Airport", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            panel.add(new JLabel("Code:"));
            JTextField codeField = new JTextField((String) airportsModel.getValueAt(selectedRow, 0));
            panel.add(codeField);
            
            panel.add(new JLabel("Name:"));
            JTextField nameField = new JTextField((String) airportsModel.getValueAt(selectedRow, 1));
            panel.add(nameField);
            
            panel.add(new JLabel("City:"));
            JTextField cityField = new JTextField((String) airportsModel.getValueAt(selectedRow, 2));
            panel.add(cityField);
            
            panel.add(new JLabel("State:"));
            JTextField stateField = new JTextField((String) airportsModel.getValueAt(selectedRow, 3));
            panel.add(stateField);
            
            panel.add(new JLabel("Country:"));
            JTextField countryField = new JTextField((String) airportsModel.getValueAt(selectedRow, 4));
            panel.add(countryField);
            
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(_ -> {
                airportsModel.setValueAt(codeField.getText(), selectedRow, 0);
                airportsModel.setValueAt(nameField.getText(), selectedRow, 1);
                airportsModel.setValueAt(cityField.getText(), selectedRow, 2);
                airportsModel.setValueAt(stateField.getText(), selectedRow, 3);
                airportsModel.setValueAt(countryField.getText(), selectedRow, 4);
                dialog.dispose();
            });
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(_ -> dialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an airport to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedAirport() {
        int selectedRow = airportsTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this airport?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                airportsModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an airport to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showAddAirlineDialog() {
        JDialog dialog = new JDialog(this, "Add New Airline", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Code:"));
        JTextField codeField = new JTextField();
        panel.add(codeField);
        
        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);
        
        panel.add(new JLabel("Headquarters:"));
        JTextField hqField = new JTextField();
        panel.add(hqField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            airlinesModel.addRow(new Object[]{
                codeField.getText(),
                nameField.getText(),
                hqField.getText()
            });
            dialog.dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editSelectedAirline() {
        int selectedRow = airlinesTable.getSelectedRow();
        if (selectedRow != -1) {
            JDialog dialog = new JDialog(this, "Edit Airline", true);
            dialog.setSize(400, 200);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            panel.add(new JLabel("Code:"));
            JTextField codeField = new JTextField((String) airlinesModel.getValueAt(selectedRow, 0));
            panel.add(codeField);
            
            panel.add(new JLabel("Name:"));
            JTextField nameField = new JTextField((String) airlinesModel.getValueAt(selectedRow, 1));
            panel.add(nameField);
            
            panel.add(new JLabel("Headquarters:"));
            JTextField hqField = new JTextField((String) airlinesModel.getValueAt(selectedRow, 2));
            panel.add(hqField);
            
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(_ -> {
                airlinesModel.setValueAt(codeField.getText(), selectedRow, 0);
                airlinesModel.setValueAt(nameField.getText(), selectedRow, 1);
                airlinesModel.setValueAt(hqField.getText(), selectedRow, 2);
                dialog.dispose();
            });
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(_ -> dialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an airline to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedAirline() {
        int selectedRow = airlinesTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this airline?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                airlinesModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an airline to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void showAddAircraftDialog() {
        JDialog dialog = new JDialog(this, "Add New Aircraft", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        panel.add(nameField);
        
        panel.add(new JLabel("Model:"));
        JTextField modelField = new JTextField();
        panel.add(modelField);
        
        panel.add(new JLabel("Manufacturing Year:"));
        JTextField yearField = new JTextField();
        panel.add(yearField);
        
        panel.add(new JLabel("Capacity:"));
        JTextField capacityField = new JTextField();
        panel.add(capacityField);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            aircraftModel.addRow(new Object[]{
                nameField.getText(),
                modelField.getText(),
                yearField.getText(),
                capacityField.getText()
            });
            dialog.dispose();
        });
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void editSelectedAircraft() {
        int selectedRow = aircraftTable.getSelectedRow();
        if (selectedRow != -1) {
            JDialog dialog = new JDialog(this, "Edit Aircraft", true);
            dialog.setSize(400, 250);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            panel.add(new JLabel("Name:"));
            JTextField nameField = new JTextField((String) aircraftModel.getValueAt(selectedRow, 0));
            panel.add(nameField);
            
            panel.add(new JLabel("Model:"));
            JTextField modelField = new JTextField((String) aircraftModel.getValueAt(selectedRow, 1));
            panel.add(modelField);
            
            panel.add(new JLabel("Manufacturing Year:"));
            JTextField yearField = new JTextField(aircraftModel.getValueAt(selectedRow, 2).toString());
            panel.add(yearField);
            
            panel.add(new JLabel("Capacity:"));
            JTextField capacityField = new JTextField(aircraftModel.getValueAt(selectedRow, 3).toString());
            panel.add(capacityField);
            
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(_ -> {
                aircraftModel.setValueAt(nameField.getText(), selectedRow, 0);
                aircraftModel.setValueAt(modelField.getText(), selectedRow, 1);
                aircraftModel.setValueAt(yearField.getText(), selectedRow, 2);
                aircraftModel.setValueAt(capacityField.getText(), selectedRow, 3);
                dialog.dispose();
            });
            
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(_ -> dialog.dispose());
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an aircraft to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedAircraft() {
        int selectedRow = aircraftTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this aircraft?", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                aircraftModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an aircraft to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void blockSelectedCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to block this customer?", 
                "Confirm Action", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                customersModel.setValueAt("Blocked", selectedRow, 4);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to block.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void blacklistSelectedCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to blacklist this customer?", 
                "Confirm Action", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                customersModel.setValueAt("Blacklisted", selectedRow, 4);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to blacklist.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void activateSelectedCustomer() {
        int selectedRow = customersTable.getSelectedRow();
        if (selectedRow != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to activate this customer?", 
                "Confirm Action", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                customersModel.setValueAt("Active", selectedRow, 4);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to activate.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void loadDummyData() {
        // Add dummy data for flights
        flightsModel.addRow(new Object[]{"AA123", "JFK", "LAX", "360", "G12", "Active", "08:30"});
        flightsModel.addRow(new Object[]{"UA456", "LAX", "ORD", "240", "B5", "Delayed", "10:45"});
        flightsModel.addRow(new Object[]{"DL789", "ATL", "DFW", "120", "C7", "Departed", "12:15"});
        flightsModel.addRow(new Object[]{"SW234", "LGA", "MIA", "180", "D9", "Active", "14:30"});
        
        // Add dummy data for airports
        airportsModel.addRow(new Object[]{"JFK", "John F. Kennedy International", "New York", "NY", "USA"});
        airportsModel.addRow(new Object[]{"LAX", "Los Angeles International", "Los Angeles", "CA", "USA"});
        airportsModel.addRow(new Object[]{"ATL", "Hartsfield-Jackson Atlanta", "Atlanta", "GA", "USA"});
        airportsModel.addRow(new Object[]{"ORD", "O'Hare International", "Chicago", "IL", "USA"});
        
        // Add dummy data for airlines
        airlinesModel.addRow(new Object[]{"AA", "American Airlines", "Fort Worth, TX"});
        airlinesModel.addRow(new Object[]{"UA", "United Airlines", "Chicago, IL"});
        airlinesModel.addRow(new Object[]{"DL", "Delta Air Lines", "Atlanta, GA"});
        airlinesModel.addRow(new Object[]{"SW", "Southwest Airlines", "Dallas, TX"});
        
        // Add dummy data for aircraft
        aircraftModel.addRow(new Object[]{"Boeing 737", "737-800", "2015", "189"});
        aircraftModel.addRow(new Object[]{"Airbus A320", "A320neo", "2018", "180"});
        aircraftModel.addRow(new Object[]{"Boeing 787", "787-9 Dreamliner", "2019", "290"});
        aircraftModel.addRow(new Object[]{"Embraer E190", "E190-E2", "2020", "114"});
        
        // Add dummy data for customers
        customersModel.addRow(new Object[]{"C001", "John Smith", "john.smith@email.com", "555-123-4567", "Active", ""});
        customersModel.addRow(new Object[]{"C002", "Sarah Johnson", "sarah.j@email.com", "555-234-5678", "Active", ""});
        customersModel.addRow(new Object[]{"C003", "Michael Brown", "mike.brown@email.com", "555-345-6789", "Blocked", ""});
        customersModel.addRow(new Object[]{"C004", "Emily Davis", "emily.d@email.com", "555-456-7890", "Blacklisted", ""});
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}