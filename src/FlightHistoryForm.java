import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import model.FlightReservation;
import model.User;

public class FlightHistoryForm extends JFrame {
    private JTextArea historyArea;
    private User user;

    public FlightHistoryForm(User user) {
        this.user = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Flight Reservation History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // History Text Area
        historyArea = new JTextArea(15, 50);
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Edit Profile");

        refreshButton.addActionListener(e -> refreshHistory());
        backButton.addActionListener(e -> {
            new EditUserForm(user).setVisible(true);
            dispose();
        });

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Initial history load
        refreshHistory();
    }

    private void refreshHistory() {
        try {
            historyArea.setText(""); // Clear existing content
            ArrayList<FlightReservation> reservations = (ArrayList<FlightReservation>) user.getReservations();
            if (reservations.isEmpty()) {
                historyArea.append("No flight reservations found.\n");
                return;
            }
            for (FlightReservation reservation : reservations) {
                String qrCode = reservation.getQrCode() != null ? reservation.getQrCode() : "N/A";
                String bookingDate = reservation.getBookingDate() != null ? reservation.getBookingDate().toString() : "N/A";
                String status = reservation.getStatus() != null ? reservation.getStatus().name() : "N/A";

                String record = String.format(
                    "Reservation ID: %d | Flight ID: %d | User ID: %d | QR Code: %s | Booking Date: %s | Status: %s\n",
                    reservation.getId(),
                    reservation.getFlightId(),
                    reservation.getUserId(),
                    qrCode,
                    bookingDate,
                    status
                );
                historyArea.append(record + "\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            // Example: Load a user for testing
            User user = User.loadWithId(2); // Replace with actual user ID
            SwingUtilities.invokeLater(() -> new FlightHistoryForm(user).setVisible(true));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}