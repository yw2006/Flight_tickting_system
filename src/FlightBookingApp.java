import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FlightBookingApp extends JFrame {
    public static final Color PRIMARY_COLOR = new Color(102, 0, 153); // Purple
    public static final Color ACCENT_COLOR = new Color(240, 240, 240); // Light gray
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT_COLOR = new Color(51, 51, 51); // Dark gray
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private Flight selectedFlight;
    private ArrayList<Passenger> passengers;
    private ArrayList<String> selectedSeats;
    private int numberOfPassengers = 1;
    private PassengerPanel passengerPanel; // Reference to PassengerPanel

    public FlightBookingApp() {
        setTitle("Flight Booking Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize panels
        SearchPanel searchPanel = new SearchPanel(this);
        ResultsPanel resultsPanel = new ResultsPanel(this);
        passengerPanel = new PassengerPanel(this);
        SeatSelectionPanel seatSelectionPanel = new SeatSelectionPanel(this);
        
        // Add panels to CardLayout
        mainPanel.add(searchPanel, "search");
        mainPanel.add(resultsPanel, "results");
        mainPanel.add(passengerPanel, "passengers");
        mainPanel.add(seatSelectionPanel, "seatSelection");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "search");
    }
    
    public CardLayout getCardLayout() {
        return cardLayout;
    }
    
    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    public void setSelectedFlight(Flight flight) {
        this.selectedFlight = flight;
    }
    
    public Flight getSelectedFlight() {
        return selectedFlight;
    }
    
    public void setPassengers(ArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }
    
    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }
    
    public void setSelectedSeats(ArrayList<String> seats) {
        this.selectedSeats = seats;
    }
    
    public ArrayList<String> getSelectedSeats() {
        return selectedSeats;
    }
    
    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }
    
    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
        System.out.println("FlightBookingApp: numberOfPassengers set to " + numberOfPassengers); // Debug log
        if (passengerPanel != null) {
            passengerPanel.refresh(); // Refresh PassengerPanel when number changes
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage());
    }
}