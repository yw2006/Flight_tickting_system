import javax.swing.*;
import java.awt.*;
class RegisterPage extends JFrame {
    private JTextField fullNameField, emailField, usernameField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> countryComboBox;

    public RegisterPage() {
        setTitle("SkyJourney Airlines - Registration");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FlightBookingApp.ACCENT_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Register with SkyJourney Airlines", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(FlightBookingApp.ACCENT_COLOR);

        // Fields
        formPanel.add(createFieldPanel("Full Name:", fullNameField = new JTextField(20)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Email:", emailField = new JTextField(20)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Username:", usernameField = new JTextField(20)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Password:", passwordField = new JPasswordField(20)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Confirm Password:", confirmPasswordField = new JPasswordField(20)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFieldPanel("Phone Number:", phoneField = new JTextField(20)));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        String[] countries = {"Select Country", "United States", "Canada", "United Kingdom", "Australia", "India", "China", "Japan", "Germany", "France", "Other"};
        countryComboBox = new JComboBox<>(countries);
        formPanel.add(createFieldPanel("Country:", countryComboBox));

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 139, 87));
        registerButton.setForeground(FlightBookingApp.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(_ -> {
            if (validateRegistration()) {
                JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginPage();
            }
        });

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(FlightBookingApp.PRIMARY_COLOR);
        backButton.setForeground(FlightBookingApp.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(_ -> {
            dispose();
            new LoginPage();
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);

        panel.add(formPanel, BorderLayout.CENTER);
        add(panel);
        setVisible(true);
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(FlightBookingApp.ACCENT_COLOR);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setPreferredSize(new Dimension(120, 25));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setPreferredSize(new Dimension(200, 25));
        }
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private boolean validateRegistration() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String phone = phoneField.getText().trim();
        String country = countryComboBox.getSelectedItem().toString();

        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your full name", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (username.isEmpty() || username.length() < 4) {
            JOptionPane.showMessageDialog(this, "Username must be at least 4 characters", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your phone number", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (country.equals("Select Country")) {
            JOptionPane.showMessageDialog(this, "Please select your country", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}


