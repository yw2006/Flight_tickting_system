import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SeatSelectionPanel extends JPanel {
    private FlightBookingApp app;
    private JToggleButton[][] seatButtons;
    private JLabel selectedSeatsLabel;

    public SeatSelectionPanel(FlightBookingApp app) {
        this.app = app;
        setBackground(FlightBookingApp.ACCENT_COLOR);
        setLayout(new BorderLayout());
        
        // Main content with padding
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JLabel headerLabel = new JLabel("SELECT SEATS");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(FlightBookingApp.PRIMARY_COLOR);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(_ -> app.getCardLayout().show(app.getMainPanel(), "passengers"));
        
        headerPanel.add(backButton);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        headerPanel.add(headerLabel);
        
        // Seat selection grid
        JPanel seatPanel = new JPanel(new GridLayout(10, 4, 10, 10)); // 10 rows, 3 seats + aisle
        seatPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        seatButtons = new JToggleButton[10][3];
        ArrayList<String> selectedSeats = new ArrayList<>();
        
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 4; col++) {
                if (col == 1) { // Aisle
                    JLabel aisleLabel = new JLabel("");
                    seatPanel.add(aisleLabel);
                    continue;
                }
                int seatCol = col > 1 ? col - 1 : col;
                JToggleButton seatButton = new JToggleButton((row + 1) + (seatCol == 0 ? "A" : seatCol == 1 ? "B" : "C"));
                seatButton.setFont(new Font("Arial", Font.PLAIN, 12));
                seatButton.setBackground(FlightBookingApp.WHITE);
                seatButton.setForeground(FlightBookingApp.TEXT_COLOR);
                seatButton.setFocusPainted(false);
                seatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                seatButton.addActionListener(_ -> {
                    String seat = seatButton.getText();
                    if (seatButton.isSelected()) {
                        seatButton.setBackground(FlightBookingApp.PRIMARY_COLOR);
                        seatButton.setForeground(FlightBookingApp.WHITE);
                        selectedSeats.add(seat);
                    } else {
                        seatButton.setBackground(FlightBookingApp.WHITE);
                        seatButton.setForeground(FlightBookingApp.TEXT_COLOR);
                        selectedSeats.remove(seat);
                    }
                    selectedSeatsLabel.setText("Selected Seats: " + String.join(", ", selectedSeats));
                });
                
                seatButtons[row][seatCol] = seatButton;
                seatPanel.add(seatButton);
            }
        }
        
        // Selected seats display
        selectedSeatsLabel = new JLabel("Selected Seats: None");
        selectedSeatsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        selectedSeatsLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        infoPanel.add(selectedSeatsLabel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(FlightBookingApp.PRIMARY_COLOR);
        confirmButton.setForeground(FlightBookingApp.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setPreferredSize(new Dimension(120, 40));
        confirmButton.addActionListener(_ -> {
            int requiredSeats = app.getPassengers().size();
            if (selectedSeats.size() != requiredSeats) {
                JOptionPane.showMessageDialog(this, 
                    "Please select exactly " + requiredSeats + " seat" + (requiredSeats > 1 ? "s" : "") + ".", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            app.setSelectedSeats(selectedSeats);
            Flight flight = app.getSelectedFlight();
            StringBuilder confirmation = new StringBuilder("Booking Confirmation\n\n");
            confirmation.append("Flight Details:\n")
                        .append("Airline: ").append(flight.airline).append("\n")
                        .append("From: ").append(flight.from).append(" (").append(flight.depAirport).append(")\n")
                        .append("To: ").append(flight.to).append(" (").append(flight.arrAirport).append(")\n")
                        .append("Departure: ").append(flight.depTime).append("\n")
                        .append("Arrival: ").append(flight.arrTime).append("\n")
                        .append("Price: ").append(flight.price).append("\n\n")
                        .append("Passengers:\n");
            for (Passenger p : app.getPassengers()) {
                confirmation.append(p.toString()).append("\n");
            }
            confirmation.append("\nSeats: ").append(String.join(", ", selectedSeats));
            JOptionPane.showMessageDialog(this, confirmation.toString(), "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            app.getCardLayout().show(app.getMainPanel(), "search");
        });
        
        buttonPanel.add(confirmButton);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(seatPanel, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.SOUTH);
        contentPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }
}