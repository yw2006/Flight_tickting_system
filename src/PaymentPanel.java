import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import model.*;

public class PaymentPanel extends JPanel {
    private FlightBookingApp app;
    private JComboBox<PaymentMethod> paymentMethodCombo;
    private JTextField amountField;

    public PaymentPanel(FlightBookingApp app) {
        this.app = app;
        setBackground(FlightBookingApp.ACCENT_COLOR);
        setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JLabel headerLabel = new JLabel("PAYMENT DETAILS");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(FlightBookingApp.PRIMARY_COLOR);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(_ -> app.navigateTo("seatSelection"));
        
        headerPanel.add(backButton);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        headerPanel.add(headerLabel);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        // Payment Method
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        methodPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        paymentMethodCombo = new JComboBox<>(PaymentMethod.values());
        paymentMethodCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        methodPanel.add(methodLabel);
        methodPanel.add(paymentMethodCombo);
        
        // Amount (calculated based on flight)
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amountPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        JLabel amountLabel = new JLabel("Amount ($):");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField = new JTextField(10);
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setEditable(false);
        double price = calculatePrice();
        amountField.setText(String.format("%.2f", price));
        amountPanel.add(amountLabel);
        amountPanel.add(amountField);
        
        formPanel.add(methodPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(amountPanel);
        
        // Pay Button
        JButton payButton = new JButton("Pay Now");
        payButton.setFont(new Font("Arial", Font.BOLD, 14));
        payButton.setBackground(FlightBookingApp.PRIMARY_COLOR);
        payButton.setForeground(FlightBookingApp.WHITE);
        payButton.setFocusPainted(false);
        payButton.setBorderPainted(false);
        payButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        payButton.setPreferredSize(new Dimension(120, 40));
        payButton.addActionListener(_ -> {
            try {
                PaymentMethod method = (PaymentMethod) paymentMethodCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                app.createReservation();
                app.processPayment(method, amount);
                app.navigateTo("confirmation");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error processing payment: " + e.getMessage(), 
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid amount entered.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        buttonPanel.add(payButton);
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private double calculatePrice() {
        // Placeholder: Base price + premium for seats
        double basePrice = 200.0;
        for (Seat seat : app.getSelectedSeats()) {
            switch (seat.getSeatClass()) {
                case FirstClass:
                    basePrice += 200;
                    break;
                case Business:
                    basePrice += 100;
                    break;
                case PremiumEconomy:
                    basePrice += 50;
                    break;
                default:
                    break;
            }
        }
        return basePrice * app.getNumberOfPassengers();
    }
}