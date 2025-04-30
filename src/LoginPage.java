import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("SkyJourney Airlines - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FlightBookingApp.ACCENT_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("SkyJourney Airlines", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(FlightBookingApp.ACCENT_COLOR);

        // Username
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        // Password
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(FlightBookingApp.PRIMARY_COLOR);
        loginButton.setForeground(FlightBookingApp.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(_ -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password", "Login Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + username, "Login Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new FlightBookingApp().setVisible(true);
            }
        });

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 139, 87)); // Sea green
        registerButton.setForeground(FlightBookingApp.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.addActionListener(_ -> {
            dispose();
            new RegisterPage();
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Forgot Password
        JLabel forgotPasswordLabel = new JLabel("Forgot Password?");
        forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPasswordLabel.setForeground(FlightBookingApp.PRIMARY_COLOR);
        forgotPasswordLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPasswordLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginPage.this, "Password reset functionality would be implemented here.", "Forgot Password", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        forgotPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        forgotPanel.add(forgotPasswordLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(usernamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(forgotPanel);

        panel.add(formPanel, BorderLayout.CENTER);
        add(panel);
        setVisible(true);
    }
}
