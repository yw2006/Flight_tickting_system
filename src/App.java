import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class App extends JFrame {

    private JTextField nameField, emailField;
    private JPasswordField oldPasswordField, passwordField;
    private JTextArea historyArea;

    public App() {
        setTitle("Customer Profile Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel editProfilePanel = createEditProfilePanel();
        tabbedPane.addTab("Edit Profile", editProfilePanel);

        JPanel historyPanel = createHistoryPanel();
        tabbedPane.addTab("Show History", historyPanel);

        add(tabbedPane);
    }

    private JPanel createEditProfilePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);
        nameField.setText("seif");

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        emailField.setText("seif@123");

        JLabel oldPasswordLabel = new JLabel("Old Password:");
        oldPasswordField = new JPasswordField(20);

        JLabel passwordLabel = new JLabel("New Password:");
        passwordField = new JPasswordField(20);

        JButton updateButton = new JButton("Update Profile");

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String oldPassword = new String(oldPasswordField.getPassword());
                String newPassword = new String(passwordField.getPassword());

                if (name.isEmpty() || email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Simple mock check — in real application, check against DB
                    if (!oldPassword.equals("existingPassword")) {
                        JOptionPane.showMessageDialog(null, "Old password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    JOptionPane.showMessageDialog(null, "Profile Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(oldPasswordLabel, gbc);
        gbc.gridx = 1;
        panel.add(oldPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(updateButton, gbc);

        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        historyArea = new JTextArea();
        historyArea.setEditable(false);

        ArrayList<String> mockHistory = getMockHistory();
        for (String record : mockHistory) {
            historyArea.append(record + "\n\n");
        }

        JScrollPane scrollPane = new JScrollPane(historyArea);

        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private ArrayList<String> getMockHistory() {
        ArrayList<String> history = new ArrayList<>();
        history.add("Flight: AI203 | From: New York -> To: London | Date: 2025-05-05 | Status: Completed");
        history.add("Flight: BA409 | From: Paris -> To: Rome | Date: 2025-06-12 | Status: Cancelled");
        history.add("Flight: EK501 | From: Dubai -> To: Mumbai | Date: 2025-07-01 | Status: Upcoming");
        return history;
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        oldPasswordField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }
}
