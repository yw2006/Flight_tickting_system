import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.FlightReservation;
import model.User;
import util.DbConnection;

public class FlightHistoryForm extends JFrame {
    private JTextArea historyArea;
    private DefaultTableModel tableModel;
    private User user;
    private JTextField reservationIdField;

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

        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        tableModel.addColumn("Reservation ID");
        tableModel.addColumn("Flight ID");
        tableModel.addColumn("User ID");
        tableModel.addColumn("QR Code");
        tableModel.addColumn("Booking Date");
        tableModel.addColumn("Status");

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        JButton backButton = new JButton("Back to Edit Profile");
        reservationIdField = new JTextField(10);
        JButton cancelButton = new JButton("Cancel");

        refreshButton.addActionListener(e -> refreshHistory());
        backButton.addActionListener(e -> {
            new EditUserForm(user).setVisible(true);
            dispose();
        });
        cancelButton.addActionListener(e -> cancelReservation());

        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);
        buttonPanel.add(new JLabel("Reservation ID:"));
        buttonPanel.add(reservationIdField);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        refreshHistory();
    }

    private void refreshHistory() {
        try {
            tableModel.setRowCount(0);

            // Reload the user and its reservations fresh from DB
            user = User.loadWithId(user.getId());
            ArrayList<FlightReservation> reservations = (ArrayList<FlightReservation>) user.getReservations();

            if (reservations == null || reservations.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No flight reservations found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            for (FlightReservation reservation : reservations) {
                String qrCode = reservation.getQrCode() != null ? reservation.getQrCode() : "N/A";
                String bookingDate = reservation.getBookingDate() != null ? reservation.getBookingDate().toString() : "N/A";
                String status = reservation.getStatus() != null ? reservation.getStatus().name() : "N/A";

                tableModel.addRow(new Object[]{
                    reservation.getId(),
                    reservation.getFlightId(),
                    reservation.getUserId(),
                    qrCode,
                    bookingDate,
                    status
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelReservation() {
        try {
            String idText = reservationIdField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a reservation ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int reservationId = Integer.parseInt(idText);
            String sql = "UPDATE flightReservation SET status = ? WHERE id = ?";
            try (Connection conn = DbConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "Canceled");
                stmt.setInt(2, reservationId);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Reservation " + reservationId + " cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshHistory();
                    reservationIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "No reservation found with ID " + reservationId + ".", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Reservation ID must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cancelling reservation: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            User user = User.loadWithId(2);
            SwingUtilities.invokeLater(() -> new FlightHistoryForm(user).setVisible(true));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
