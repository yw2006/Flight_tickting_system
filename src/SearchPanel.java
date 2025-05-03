import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

public class SearchPanel extends JPanel {
    private FlightBookingApp app;
    private JTextField fromField;
    private JTextField toField;
    private DatePicker departDateField;
    private JComboBox<String> passengerComboBox;

    public SearchPanel(FlightBookingApp app) {
        this.app = app;
        setBackground(FlightBookingApp.ACCENT_COLOR);
        setLayout(new BorderLayout());
        
        // Main content with padding
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JLabel planeIcon = new JLabel("✈");
        planeIcon.setFont(new Font("Arial", Font.PLAIN, 22));
        
        JLabel headerLabel = new JLabel("BOOK YOUR FLIGHT");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        headerPanel.add(planeIcon);
        headerPanel.add(headerLabel);
        
        // Form panel with search options
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        // Trip type toggle buttons
        JPanel tripTypePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tripTypePanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        
        
        
        // Search fields panel
        JPanel searchFieldsPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        searchFieldsPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        // Create fields
        fromField = createSearchField("From", "Milano");
        toField = createSearchField("To", "Madrid");
        departDateField = createDateField("Depart Date");
        
        // Passenger selection
        JPanel passengerPanel = new JPanel(new BorderLayout());
        passengerPanel.setBackground(FlightBookingApp.ACCENT_COLOR);

        String[] passengerOptions = {"Number of passengers","1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        passengerComboBox = new JComboBox<>(passengerOptions);
        passengerComboBox.setFont(new Font("Arial", Font.PLAIN, 15));
        passengerComboBox.setVisible(true);
        passengerComboBox.addActionListener(_ -> {
int count = Integer.parseInt((String) passengerComboBox.getSelectedItem());
            app.setNumberOfPassengers(count);
            passengerComboBox.setVisible(true);
            
        });
        passengerPanel.add(passengerComboBox, BorderLayout.SOUTH);
        searchFieldsPanel.add(fromField);
        searchFieldsPanel.add(toField);
        searchFieldsPanel.add(departDateField);
        searchFieldsPanel.add(passengerPanel);
        
        // Search button
        JButton searchButton = new JButton("Show flights");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(FlightBookingApp.PRIMARY_COLOR);
        searchButton.setForeground(FlightBookingApp.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.addActionListener(_ -> {
            String from = fromField.getText();
            String to = toField.getText();
            if (from.isEmpty() || to.isEmpty() || from.equals("From") || to.equals("To")) {
                JOptionPane.showMessageDialog(this, "Please enter valid origin and destination.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (departDateField.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select a departure date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(passengerComboBox.getSelectedItem().equals("Number of passengers")){
                JOptionPane.showMessageDialog(this, "Please select a number for passengers.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Navigate to ResultsPanel and refresh it
            app.getCardLayout().show(app.getMainPanel(), "results");
            ResultsPanel resultsPanel = (ResultsPanel) app.getMainPanel().getComponent(1); // Assuming ResultsPanel is at index 1
            resultsPanel.refresh(this);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        buttonPanel.add(searchButton);
        
        // Add all parts to form panel
        formPanel.add(tripTypePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(searchFieldsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(buttonPanel);
        
        // Add header and form to content panel
        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add content panel to search panel
        add(contentPanel, BorderLayout.NORTH);
    }
    
    // private JToggleButton createToggleButton(String text, boolean selected) {
    //     JToggleButton button = new JToggleButton(text);
    //     button.setFont(new Font("Arial", Font.BOLD, 12));
    //     button.setSelected(selected);
    //     button.setFocusPainted(false);
    //     button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    //     button.setPreferredSize(new Dimension(100, 30));
        
    //     if (selected) {
    //         button.setBackground(FlightBookingApp.PRIMARY_COLOR);
    //         button.setForeground(FlightBookingApp.WHITE);
    //     } else {
    //         button.setBackground(FlightBookingApp.WHITE);
    //         button.setForeground(FlightBookingApp.TEXT_COLOR);
    //     }
        
    //     button.addActionListener(_ -> {
    //         if (button.isSelected()) {
    //             button.setBackground(FlightBookingApp.PRIMARY_COLOR);
    //             button.setForeground(FlightBookingApp.WHITE);
    //         } else {
    //             button.setBackground(FlightBookingApp.WHITE);
    //             button.setForeground(FlightBookingApp.TEXT_COLOR);
    //         }
    //     });
        
    //     return button;
    // }
    
    private JTextField createSearchField(String placeholder, String defaultValue) {
        JTextField field = new JTextField(defaultValue);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(200, 40));
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                }
            }
        });
        
        return field;
    }
    
    private DatePicker createDateField(String placeholder) {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setFontCalendarDateLabels(new Font("Arial", Font.PLAIN, 14));
        settings.setFontValidDate(new Font("Arial", Font.PLAIN, 14));
        settings.setFontVetoedDate(new Font("Arial", Font.PLAIN, 14));
        settings.setFontMonthAndYearMenuLabels(new Font("Arial", Font.BOLD, 14));
        settings.setColor(DatePickerSettings.DateArea.BackgroundOverallCalendarPanel, FlightBookingApp.WHITE);
        settings.setColor(DatePickerSettings.DateArea.DatePickerTextValidDate, FlightBookingApp.TEXT_COLOR);
        settings.setColor(DatePickerSettings.DateArea.BackgroundMonthAndYearNavigationButtons, FlightBookingApp.PRIMARY_COLOR);
        settings.setColor(DatePickerSettings.DateArea.TextMonthAndYearMenuLabels, FlightBookingApp.WHITE);
        
        DatePicker datePicker = new DatePicker(settings);
        datePicker.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        datePicker.setPreferredSize(new Dimension(200, 40));
        
        datePicker.getComponentDateTextField().setText(placeholder);
        datePicker.getComponentDateTextField().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (datePicker.getDate() == null && datePicker.getComponentDateTextField().getText().equals(placeholder)) {
                    datePicker.getComponentDateTextField().setText("");
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (datePicker.getDate() == null) {
                    datePicker.getComponentDateTextField().setText(placeholder);
                }
            }
        });
        
        return datePicker;
    }

    // Getters for search data
    public String getFrom() {
        return fromField.getText();
    }

    public String getTo() {
        return toField.getText();
    }

    public LocalDate getDepartDate() {
        return departDateField.getDate();
    }
}