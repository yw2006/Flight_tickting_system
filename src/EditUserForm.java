
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import model.User;

public class EditUserForm extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField ageField;
    private User user;

    public EditUserForm(User user) {
        this.user = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("Edit User Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(user.getUsername(), 20);
        panel.add(usernameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(user.getEmail(), 20);
        panel.add(emailField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(user.getPhone(), 20);
        panel.add(phoneField, gbc);

        // Age
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(user.getAge() != null ? user.getAge().toString() : "", 20);
        panel.add(ageField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        JButton viewHistoryButton = new JButton("View Reservation History");

        saveButton.addActionListener(e -> saveUserDetails());
        cancelButton.addActionListener(e -> dispose());
        viewHistoryButton.addActionListener(e -> openHistoryPage());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(viewHistoryButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        add(panel);
    }

    private void saveUserDetails() {
        try {
            String newUsername = usernameField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newPhone = phoneField.getText().trim();
            String ageText = ageField.getText().trim();
            Integer newAge = ageText.isEmpty() ? null : Integer.parseInt(ageText);

            // Basic validation
            if (newEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Email cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            user.update(newUsername, newEmail, newPhone, newAge);
            JOptionPane.showMessageDialog(this, "User details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openHistoryPage() {
        new FlightHistoryForm(user).setVisible(true);
    }

    public static void main(String[] args) {
        try {
            // Example: Load a user for testing
            User user = User.loadWithId(2); // Replace with actual user ID
            SwingUtilities.invokeLater(() -> new EditUserForm(user).setVisible(true));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}