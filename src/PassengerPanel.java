import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import model.*;

public class PassengerPanel extends JPanel {
    private FlightBookingApp app;
    private JPanel passengerDetailsPanel;
    private ArrayList<JTextField> fullNameFields;
    private ArrayList<JTextField> passportFields;
    private JLabel countLabel;

    public PassengerPanel(FlightBookingApp app) {
        this.app = app;
        setBackground(FlightBookingApp.ACCENT_COLOR);
        setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JLabel headerLabel = new JLabel("ADD PASSENGER DETAILS");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(FlightBookingApp.PRIMARY_COLOR);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(_ -> app.navigateTo("results"));
        
        headerPanel.add(backButton);
        headerPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        headerPanel.add(headerLabel);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JPanel countPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        countPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        countLabel = new JLabel("Passengers: " + app.getNumberOfPassengers());
        countLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        countPanel.add(countLabel);
        
        passengerDetailsPanel = new JPanel();
        passengerDetailsPanel.setLayout(new BoxLayout(passengerDetailsPanel, BoxLayout.Y_AXIS));
        passengerDetailsPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        fullNameFields = new ArrayList<>();
        passportFields = new ArrayList<>();
        updatePassengerFields(app.getNumberOfPassengers());
        
        JButton nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setBackground(FlightBookingApp.PRIMARY_COLOR);
        nextButton.setForeground(FlightBookingApp.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setPreferredSize(new Dimension(120, 40));
        nextButton.addActionListener(_ -> {
            ArrayList<Passenger> newPassengers = new ArrayList<>();
            for (int i = 0; i < app.getNumberOfPassengers(); i++) {
                String fullName = fullNameFields.get(i).getText().trim();
                String passportNumber = passportFields.get(i).getText().trim();
                if (fullName.isEmpty() || passportNumber.isEmpty()) {
                    JOptionPane.showMessageDialog(this, 
                        "Please enter full name and passport number for all passengers.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Passenger passenger = new Passenger(fullName, passportNumber);
                passenger.setUserId(app.getCurrentUser().getId());
                newPassengers.add(passenger);
            }
            app.setPassengers(newPassengers);
            app.navigateTo("seatSelection");
            new SeatSelectionPanel(app).refresh();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        buttonPanel.add(nextButton);
        
        formPanel.add(countPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(passengerDetailsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);
        
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void refresh() {
        countLabel.setText("Passengers: " + app.getNumberOfPassengers());
        updatePassengerFields(app.getNumberOfPassengers());
    }
    
    private void updatePassengerFields(int totalPassengers) {
        passengerDetailsPanel.removeAll();
        fullNameFields.clear();
        passportFields.clear();
        
        for (int i = 0; i < totalPassengers; i++) {
            JPanel passengerRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            passengerRow.setBackground(FlightBookingApp.ACCENT_COLOR);
            
            JLabel passengerLabel = new JLabel("Passenger " + (i + 1) + ":");
            passengerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JTextField fullNameField = new JTextField(20);
            fullNameField.setFont(new Font("Arial", Font.PLAIN, 14));
            fullNameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            fullNameField.setToolTipText("Enter full name");
            
            JTextField passportField = new JTextField(10);
            passportField.setFont(new Font("Arial", Font.PLAIN, 14));
            passportField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            passportField.setToolTipText("Enter passport number");
            
            passengerRow.add(passengerLabel);
            passengerRow.add(fullNameField);
            passengerRow.add(passportField);
            
            fullNameFields.add(fullNameField);
            passportFields.add(passportField);
            
            passengerDetailsPanel.add(passengerRow);
            passengerDetailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        passengerDetailsPanel.revalidate();
        passengerDetailsPanel.repaint();
    }
}