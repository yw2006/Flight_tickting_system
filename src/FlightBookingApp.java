
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.*;

/**
 * Main application frame for the Flight Booking System
 * Contains the CardLayout to switch between different panels/screens
 */
public class FlightBookingApp extends JFrame {
    // Application-wide color scheme
    public static final Color PRIMARY_COLOR = new Color(102, 0, 153); // Purple
    public static final Color ACCENT_COLOR = new Color(240, 240, 240); // Light gray
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT_COLOR = new Color(51, 51, 51); // Dark gray
    public static final Color SUCCESS_COLOR = new Color(46, 125, 50); // Green
    public static final Color WARNING_COLOR = new Color(237, 108, 2); // Orange
    public static final Color ERROR_COLOR = new Color(211, 47, 47); // Red
    
    // Font definitions
    public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 20);
    public static final Font SUBHEADER_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font("Arial", Font.PLAIN, 12);
    
    // Main panels
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    // User data
    private User currentUser;
    
    // Flight booking data
    private Flight selectedFlight;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Aircraft selectedAircraft;
    private WeeklySchedule selectedSchedule;
    private List<Passenger> passengers;
    private List<Seat> selectedSeats;
    private FlightReservation currentReservation;
    private Payment currentPayment;
    
    // References to panels
    private SearchPanel searchPanel;
    private ResultsPanel resultsPanel;
    private PassengerPanel passengerPanel;
    private SeatSelectionPanel seatSelectionPanel;
    private PaymentPanel paymentPanel;
    private ConfirmationPanel confirmationPanel;
    
    /**
     * Constructor - initializes the main application frame
     */
    public FlightBookingApp(User user) {
        this.currentUser = user;
        this.passengers = new ArrayList<>();
        this.selectedSeats = new ArrayList<>();
        
        // Initialize the main frame properties
        setTitle("Flight Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Initialize main container with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize all panels
        initializePanels();
        
        // Add the main panel to the frame
        add(mainPanel);
        
        // Display the search panel initially
        cardLayout.show(mainPanel, "search");
        
        // Make the frame visible
        setVisible(true);
    }
    
    /**
     * Initialize all application panels and add them to the CardLayout
     */
    private void initializePanels() {
        // Create panels
        searchPanel = new SearchPanel(this);
        resultsPanel = new ResultsPanel(this);
        passengerPanel = new PassengerPanel(this);
        seatSelectionPanel = new SeatSelectionPanel(this);
        paymentPanel = new PaymentPanel(this);
        confirmationPanel = new ConfirmationPanel(this);
        
        // Add panels to the CardLayout
        mainPanel.add(searchPanel, "search");
        mainPanel.add(resultsPanel, "results");
        mainPanel.add(passengerPanel, "passengers");
        mainPanel.add(seatSelectionPanel, "seatSelection");
        mainPanel.add(paymentPanel,"payment");
        mainPanel.add(confirmationPanel, "confirmation");
    }
    
    /**
     * Navigate to a specific panel
     * @param panelName the name of the panel to navigate to
     */
    public void navigateTo(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    /**
     * Reset all booking data (useful when starting a new booking)
     */
    public void resetBookingData() {
        selectedFlight = null;
        departureAirport = null;
        arrivalAirport = null;
        selectedAircraft = null;
        selectedSchedule = null;
        passengers.clear();
        selectedSeats.clear();
        currentReservation = null;
        currentPayment = null;
    }
    
    /**
     * Create a new flight reservation based on current selections
     */
    public void createReservation() throws SQLException {
        if (selectedFlight == null || passengers.isEmpty() || selectedSeats.isEmpty()) {
            throw new IllegalStateException("Incomplete booking data");
        }
        currentReservation = new FlightReservation(selectedFlight.getId(), new java.sql.Date(System.currentTimeMillis()));
        currentReservation.setUserId(currentUser.getId());
        currentReservation.setQrCode(generateQrCode());
        currentReservation.save();
        
        for (int i = 0; i < passengers.size(); i++) {
            Passenger passenger = passengers.get(i);
            passenger.setFlightReservationId(currentReservation.getId());
            passenger.save();
            Seat seat = selectedSeats.get(i);
            currentReservation.addPassengerSeat(passenger, seat);
        }
        currentReservation.makeReservation();
    }
    
    /**
     * Process payment for the current reservation
     */
    public void processPayment(PaymentMethod method, double amount) throws SQLException {
        if (currentReservation == null) {
            throw new IllegalStateException("No reservation to pay for");
        }
        currentPayment = new Payment(amount, method, new java.sql.Date(System.currentTimeMillis()));
        currentPayment.setUserId(currentUser.getId());
        currentPayment.save();
        currentPayment.makeTransaction();
        selectedFlight.addPayment(currentPayment);
    }
    
    /**
     * Generate a simple QR code (placeholder)
     */
    private String generateQrCode() {
        return "QR_" + System.currentTimeMillis() + "_" + currentUser.getId();
    }
    
    // Getters and setters
    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }
    public Flight getSelectedFlight() { return selectedFlight; }
    public void setSelectedFlight(Flight flight) { this.selectedFlight = flight; }
    public Airport getDepartureAirport() { return departureAirport; }
    public void setDepartureAirport(Airport departureAirport) { this.departureAirport = departureAirport; }
    public Airport getArrivalAirport() { return arrivalAirport; }
    public void setArrivalAirport(Airport arrivalAirport) { this.arrivalAirport = arrivalAirport; }
    public Aircraft getSelectedAircraft() { return selectedAircraft; }
    public void setSelectedAircraft(Aircraft aircraft) { this.selectedAircraft = aircraft; }
    public WeeklySchedule getSelectedSchedule() { return selectedSchedule; }
    public void setSelectedSchedule(WeeklySchedule schedule) { this.selectedSchedule = schedule; }
    public List<Passenger> getPassengers() { return passengers; }
    public void setPassengers(List<Passenger> passengers) { this.passengers = passengers; }
    public List<Seat> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(List<Seat> seats) { this.selectedSeats = seats; }
    public int getNumberOfPassengers() { return passengers.size(); }
    public FlightReservation getCurrentReservation() { return currentReservation; }
    public void setCurrentReservation(FlightReservation reservation) { this.currentReservation = reservation; }
    public Payment getCurrentPayment() { return currentPayment; }
    public void setCurrentPayment(Payment payment) { this.currentPayment = payment; }
    public CardLayout getCardLayout() { return cardLayout; }
    public JPanel getMainPanel() { return mainPanel; }
}