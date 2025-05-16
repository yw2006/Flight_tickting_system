import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.*;
import util.DbConnection;

public class SeatSelectionPanel extends JPanel {
    private FlightBookingApp app;
    private JToggleButton[][] seatButtons;
    private JLabel selectedSeatsLabel;
    private List<Seat> availableSeats;

    public SeatSelectionPanel(FlightBookingApp app) {
        this.app = app;
        this.availableSeats = new ArrayList<>();
        setBackground(FlightBookingApp.ACCENT_COLOR);
        setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
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
        backButton.addActionListener(_ -> app.navigateTo("passengers"));
        
        headerPanel.add(backButton);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        headerPanel.add(headerLabel);
        
        JPanel seatPanel = new JPanel(new GridLayout(10, 4, 10, 10));
        seatPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        seatButtons = new JToggleButton[10][3];
        List<String> selectedSeatNumbers = new ArrayList<>();
        
        // Initialize with placeholder seat buttons (will be updated in refresh)
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 4; col++) {
                if (col == 1) {
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
                seatButton.setEnabled(false);
                
                seatButtons[row][seatCol] = seatButton;
                seatPanel.add(seatButton);
            }
        }
        
        selectedSeatsLabel = new JLabel("Selected Seats: None");
        selectedSeatsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        selectedSeatsLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        infoPanel.add(selectedSeatsLabel);
        
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
            if (app.getSelectedSeats().size() != requiredSeats) {
                JOptionPane.showMessageDialog(this, 
                    "Please select exactly " + requiredSeats + " seat" + (requiredSeats > 1 ? "s" : "") + ".", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            app.navigateTo("payment");
        });
        
        buttonPanel.add(confirmButton);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(seatPanel, BorderLayout.CENTER);
        contentPanel.add(infoPanel, BorderLayout.SOUTH);
        contentPanel.add(buttonPanel, BorderLayout.EAST);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void refresh() {
        try {
            availableSeats = loadAvailableSeats();
            updateSeatButtons();
            selectedSeatsLabel.setText("Selected Seats: None");
            app.setSelectedSeats(new ArrayList<>());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading seats: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private List<Seat> loadAvailableSeats() throws SQLException {
        List<Seat> seats = app.getSelectedAircraft().getSeats();
        List<Seat> available = new ArrayList<>();
        String sql = "SELECT seat_id FROM passenger_seat ps " +
                     "JOIN flightReservation fr ON ps.flightReservation_id = fr.id " +
                     "WHERE fr.flight_id = ?";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, app.getSelectedFlight().getId());
            try (ResultSet rs = stmt.executeQuery()) {
                List<Integer> takenSeatIds = new ArrayList<>();
                while (rs.next()) {
                    takenSeatIds.add(rs.getInt("seat_id"));
                }
                for (Seat seat : seats) {
                    if (!takenSeatIds.contains(seat.getId())) {
                        available.add(seat);
                    }
                }
            }
        }
        return available;
    }
    
    private void updateSeatButtons() {
        List<String> selectedSeatNumbers = new ArrayList<>();
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 3; col++) {
                JToggleButton button = seatButtons[row][col];
                String seatNumber = (row + 1) + (col == 0 ? "A" : col == 1 ? "B" : "C");
                Seat seat = availableSeats.stream()
                    .filter(s -> s.getId().equals(seatNumber))
                    .findFirst()
                    .orElse(null);
                
                button.setText(seatNumber);
                button.setEnabled(seat != null);
                button.setBackground(FlightBookingApp.WHITE);
                button.setForeground(FlightBookingApp.TEXT_COLOR);
                
                button.removeActionListener(button.getActionListeners()[0]);
                button.addActionListener(_ -> {
                    if (button.isSelected()) {
                        if (app.getSelectedSeats().size() >= app.getPassengers().size()) {
                            button.setSelected(false);
                            return;
                        }
                        button.setBackground(FlightBookingApp.PRIMARY_COLOR);
                        button.setForeground(FlightBookingApp.WHITE);
                        selectedSeatNumbers.add(seatNumber);
                        app.getSelectedSeats().add(seat);
                    } else {
                        button.setBackground(FlightBookingApp.WHITE);
                        button.setForeground(FlightBookingApp.TEXT_COLOR);
                        selectedSeatNumbers.remove(seatNumber);
                        app.getSelectedSeats().remove(seat);
                    }
                    selectedSeatsLabel.setText("Selected Seats: " + String.join(", ", selectedSeatNumbers));
                });
            }
        }
    }
}