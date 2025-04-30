import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;

public class ResultsPanel extends JPanel {
    private FlightBookingApp app;
    private JPanel flightsListPanel;
    private JLabel routeLabel;

    public ResultsPanel(FlightBookingApp app) {
        this.app = app;
        setBackground(FlightBookingApp.ACCENT_COLOR);
        setLayout(new BorderLayout());
        
        // Main content panel with padding
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        // Route information panel
        JPanel routePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        routePanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        routeLabel = new JLabel("Select a flight");
        routeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        routeLabel.setForeground(FlightBookingApp.TEXT_COLOR);
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(FlightBookingApp.PRIMARY_COLOR);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(_ -> app.getCardLayout().show(app.getMainPanel(), "search"));
        
        routePanel.add(backButton);
        routePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        routePanel.add(routeLabel);
        
        // Results area - split into two panels
        JPanel resultsAreaPanel = new JPanel(new BorderLayout(20, 0));
        resultsAreaPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        // Filters panel (right side)
        JPanel filtersPanel = createFiltersPanel();
        
        // Flights list panel (left/center)
        flightsListPanel = new JPanel();
        flightsListPanel.setLayout(new BoxLayout(flightsListPanel, BoxLayout.Y_AXIS));
        flightsListPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(flightsListPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        resultsAreaPanel.add(scrollPane, BorderLayout.CENTER);
        resultsAreaPanel.add(filtersPanel, BorderLayout.EAST);
        
        // Add everything to content panel
        contentPanel.add(routePanel, BorderLayout.NORTH);
        contentPanel.add(resultsAreaPanel, BorderLayout.CENTER);
        
        // Add content panel to results panel
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void refresh(SearchPanel searchPanel) {
        // Clear existing flights
        flightsListPanel.removeAll();
        
        // Update route label and add example flights
        String from = searchPanel.getFrom();
        String to = searchPanel.getTo();
        updateRouteLabel(from, to, searchPanel.getDepartDate());
        addFlightCard("Alitalia", from, to, "7:30 AM", "9:55 AM", "MXP", "MAD", "2h 25m", "Nonstop", "$200");
        addFlightCard("Alitalia", from, to, "8:30 PM", "10:25 PM", "MXP", "MAD", "2h 25m", "Nonstop", "$234");
        
        // Refresh UI
        flightsListPanel.revalidate();
        flightsListPanel.repaint();
    }
    
    private JPanel createFiltersPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FlightBookingApp.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setPreferredSize(new Dimension(250, 0));
        
        // Sort by dropdown
        JPanel sortPanel = new JPanel(new BorderLayout(0, 10));
        sortPanel.setBackground(FlightBookingApp.WHITE);
        
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        String[] sortOptions = {"Lowest Price", "Earliest Departure", "Latest Departure", "Shortest Duration"};
        JComboBox<String> sortByComboBox = new JComboBox<>(sortOptions);
        sortByComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        sortPanel.add(sortLabel, BorderLayout.NORTH);
        sortPanel.add(sortByComboBox, BorderLayout.CENTER);
        
        // Arrival time filter
        JPanel arrivalTimePanel = createFilterSection("Arrival Time", new String[]{"5:00 AM - 11:59 AM", "12:00 PM - 5:59 PM"});
        
        // Stops filter
        JPanel stopsPanel = createFilterSection("Stops", new String[]{"Nonstop", "1 Stop", "2+ Stops"});
        
        // Airlines filter
        JPanel airlinesPanel = createFilterSection("Airlines Included", 
            new String[]{"Alitalia", "Lufthansa", "Air France", "Brussels Airlines", "Air Italy", "Siberia"});
        
        panel.add(sortPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(arrivalTimePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(stopsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(airlinesPanel);
        
        return panel;
    }
    
    private JPanel createFilterSection(String title, String[] options) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FlightBookingApp.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        ButtonGroup group = new ButtonGroup();
        
        for (String option : options) {
            JPanel optionPanel = new JPanel(new BorderLayout());
            optionPanel.setBackground(FlightBookingApp.WHITE);
            optionPanel.setMaximumSize(new Dimension(250, 30));
            
            JRadioButton radioBtn = new JRadioButton();
            radioBtn.setBackground(FlightBookingApp.WHITE);
            
            JLabel optionLabel = new JLabel(option);
            optionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JLabel priceLabel = new JLabel("$230");
            priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            priceLabel.setForeground(Color.GRAY);
            
            group.add(radioBtn);
            
            optionPanel.add(radioBtn, BorderLayout.WEST);
            optionPanel.add(optionLabel, BorderLayout.CENTER);
            optionPanel.add(priceLabel, BorderLayout.EAST);
            
            panel.add(optionPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        return panel;
    }
    
    private void updateRouteLabel(String from, String to, LocalDate departDate) {
        String routeText = from + " - " + to + ", " + app.getNumberOfPassengers() + " passenger" + 
                          (app.getNumberOfPassengers() > 1 ? "s" : "");
        if (departDate != null) {
            routeText += " (" + departDate.toString() + ")";
        }
        routeLabel.setText(routeText);
    }
    
    private void addFlightCard(String airline, String from, String to, String depTime, String arrTime, 
                              String depAirport, String arrAirport, String duration, String stops, String price) {
        
        JPanel flightCard = new JPanel(new BorderLayout(10, 0));
        flightCard.setBackground(FlightBookingApp.WHITE);
        flightCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        flightCard.setMaximumSize(new Dimension(650, 130));
        
        JPanel flightInfoPanel = new JPanel(new BorderLayout(0, 10));
        flightInfoPanel.setBackground(FlightBookingApp.WHITE);
        
        JPanel depPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        depPanel.setBackground(FlightBookingApp.WHITE);
        
        JLabel depIcon = new JLabel("✈");
        depIcon.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel depLabel = new JLabel("Departure");
        depLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        depLabel.setForeground(Color.GRAY);
        
        depPanel.add(depIcon);
        depPanel.add(depLabel);
        
        JPanel timesPanel = new JPanel(new BorderLayout());
        timesPanel.setBackground(FlightBookingApp.WHITE);
        
        JPanel timelinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        timelinePanel.setBackground(FlightBookingApp.WHITE);
        
        JLabel depTimeLabel = new JLabel(depTime);
        depTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel connectorLabel = new JLabel(" ─────────── ");
        
        JLabel airlineIconLabel = new JLabel();
        airlineIconLabel.setIcon(createAirlineIcon(airline));
        
        JLabel connectorLabel2 = new JLabel(" ─────────── ");
        
        JLabel arrTimeLabel = new JLabel(arrTime);
        arrTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        timelinePanel.add(depTimeLabel);
        timelinePanel.add(connectorLabel);
        timelinePanel.add(airlineIconLabel);
        timelinePanel.add(connectorLabel2);
        timelinePanel.add(arrTimeLabel);
        
        JPanel airportsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        airportsPanel.setBackground(FlightBookingApp.WHITE);
        
        JLabel depAirportLabel = new JLabel("Airport: " + depAirport);
        depAirportLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel spacerLabel = new JLabel("                                             ");
        
        JLabel durationLabel = new JLabel(duration + " (" + stops + ")");
        durationLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        durationLabel.setForeground(Color.GRAY);
        
        JLabel spacerLabel2 = new JLabel("                    ");
        
        JLabel arrAirportLabel = new JLabel("Airport: " + arrAirport);
        arrAirportLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        airportsPanel.add(depAirportLabel);
        airportsPanel.add(spacerLabel);
        airportsPanel.add(durationLabel);
        airportsPanel.add(spacerLabel2);
        airportsPanel.add(arrAirportLabel);
        
        timesPanel.add(timelinePanel, BorderLayout.NORTH);
        timesPanel.add(airportsPanel, BorderLayout.SOUTH);
        
        flightInfoPanel.add(depPanel, BorderLayout.NORTH);
        flightInfoPanel.add(timesPanel, BorderLayout.CENTER);
        
        JPanel pricePanel = new JPanel(new BorderLayout(0, 10));
        pricePanel.setBackground(FlightBookingApp.WHITE);
        
        JPanel priceInfoPanel = new JPanel(new BorderLayout());
        priceInfoPanel.setBackground(FlightBookingApp.WHITE);
        
        JLabel priceValueLabel = new JLabel("Price: " + price);
        priceValueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        

        
        priceInfoPanel.add(priceValueLabel, BorderLayout.NORTH);
        
        JButton bookButton = new JButton("Book Flight");
        bookButton.setFont(new Font("Arial", Font.BOLD, 12));
        bookButton.setBackground(FlightBookingApp.PRIMARY_COLOR);
        bookButton.setForeground(FlightBookingApp.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.setBorderPainted(false);
        bookButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookButton.addActionListener(_ -> {
            app.setSelectedFlight(new Flight(airline, from, to, depTime, arrTime, depAirport, arrAirport, duration, stops, price));
            app.getCardLayout().show(app.getMainPanel(), "passengers");
        });
        
        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailsPanel.setBackground(FlightBookingApp.WHITE);
        
        JLabel detailsLabel = new JLabel("Check the details");
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsLabel.setForeground(FlightBookingApp.PRIMARY_COLOR);
        detailsLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        detailsLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(
                    app,
                    "Flight Details\n\n" +
                    "Airline: " + airline + "\n" +
                    "From: " + from + " (" + depAirport + ")\n" +
                    "To: " + to + " (" + arrAirport + ")\n" +
                    "Departure: " + depTime + "\n" +
                    "Arrival: " + arrTime + "\n" +
                    "Duration: " + duration + "\n" +
                    "Type: " + stops + "\n" +
                    "Price: " + price,
                    "Flight Details",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                detailsLabel.setText("<html><u>Check the details</u></html>");
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                detailsLabel.setText("Check the details");
            }
        });
        
        detailsPanel.add(detailsLabel);
        
        pricePanel.add(priceInfoPanel, BorderLayout.NORTH);
        pricePanel.add(bookButton, BorderLayout.CENTER);
        pricePanel.add(detailsPanel, BorderLayout.SOUTH);
        
        flightCard.add(flightInfoPanel, BorderLayout.CENTER);
        flightCard.add(pricePanel, BorderLayout.EAST);
        
        JPanel cardContainer = new JPanel();
        cardContainer.setLayout(new BoxLayout(cardContainer, BoxLayout.Y_AXIS));
        cardContainer.setBackground(FlightBookingApp.ACCENT_COLOR);
        cardContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        cardContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardContainer.setMaximumSize(new Dimension(650, 140));
        
        cardContainer.add(flightCard);
        
        flightsListPanel.add(cardContainer);
    }
    
    private ImageIcon createAirlineIcon(String airline) {
        int size = 16;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        Color airlineColor;
        switch (airline) {
            case "Alitalia":
                airlineColor = new Color(0, 153, 0);
                break;
            case "Lufthansa":
                airlineColor = new Color(0, 51, 153);
                break;
            default:
                airlineColor = new Color(102, 0, 153);
        }
        
        g2d.setColor(airlineColor);
        g2d.fillOval(0, 0, size, size);
        g2d.dispose();
        
        return new ImageIcon(img);
    }
}