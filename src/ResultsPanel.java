import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.*;
import util.DbConnection;

public class ResultsPanel extends JPanel {
    private FlightBookingApp app;
    private JPanel flightsListPanel;
    private JLabel routeLabel;

    public ResultsPanel(FlightBookingApp app) {
        this.app = app;
        setBackground(FlightBookingApp.ACCENT_COLOR);
        setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        contentPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
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
        backButton.addActionListener(_ -> app.navigateTo("search"));
        
        routePanel.add(backButton);
        routePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        routePanel.add(routeLabel);
        
        JPanel resultsAreaPanel = new JPanel(new BorderLayout(20, 0));
        resultsAreaPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JPanel filtersPanel = createFiltersPanel();
        
        flightsListPanel = new JPanel();
        flightsListPanel.setLayout(new BoxLayout(flightsListPanel, BoxLayout.Y_AXIS));
        flightsListPanel.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(flightsListPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(FlightBookingApp.ACCENT_COLOR);
        
        resultsAreaPanel.add(scrollPane, BorderLayout.CENTER);
        resultsAreaPanel.add(filtersPanel, BorderLayout.EAST);
        
        contentPanel.add(routePanel, BorderLayout.NORTH);
        contentPanel.add(resultsAreaPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    public void refresh(SearchPanel searchPanel) {
        flightsListPanel.removeAll();
        
        String from = searchPanel.getFrom();
        String to = searchPanel.getTo();
        updateRouteLabel(from, to, searchPanel.getDepartDate());
        
        try {
            List<Flight> flights = searchFlights(app.getDepartureAirport(), app.getArrivalAirport(), app.getSelectedSchedule());
            for (Flight flight : flights) {
                addFlightCard(flight);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading flights: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        flightsListPanel.revalidate();
        flightsListPanel.repaint();
    }
    
    private List<Flight> searchFlights(Airport departure, Airport arrival, WeeklySchedule schedule) throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT f.id FROM flight f " +
                     "JOIN weeklySchedule ws ON f.flight_schedule_id = ws.id " +
                     "WHERE f.departure_airport_id = ? AND f.arrival_airport_id = ? " +
                     "AND (ws.customDate = ? OR ws.dayOfWeek = ?)";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, departure.getId());
            stmt.setInt(2, arrival.getId());
            stmt.setDate(3, schedule.getCustomDate());
            stmt.setString(4, schedule.getDayOfWeek().name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    flights.add(Flight.load(rs.getInt("id")));
                }
            }
        }
        return flights;
    }
    
    private JPanel createFiltersPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(FlightBookingApp.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setPreferredSize(new Dimension(250, 0));
        
        JPanel sortPanel = new JPanel(new BorderLayout(0, 10));
        sortPanel.setBackground(FlightBookingApp.WHITE);
        
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        String[] sortOptions = {"Lowest Price", "Earliest Departure", "Shortest Duration"};
        JComboBox<String> sortByComboBox = new JComboBox<>(sortOptions);
        sortByComboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        sortPanel.add(sortLabel, BorderLayout.NORTH);
        sortPanel.add(sortByComboBox, BorderLayout.CENTER);
        
        JPanel airlinesPanel = createFilterSection("Airlines Included", getAirlines());
        
        panel.add(sortPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(airlinesPanel);
        
        return panel;
    }
    
    private String[] getAirlines() {
        List<String> airlines = new ArrayList<>();
        try {
            String sql = "SELECT name FROM airline";
            try (Connection conn = DbConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    airlines.add(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading airlines: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        return airlines.toArray(new String[0]);
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
            
            group.add(radioBtn);
            optionPanel.add(radioBtn, BorderLayout.WEST);
            optionPanel.add(optionLabel, BorderLayout.CENTER);
            
            panel.add(optionPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        return panel;
    }
    
    private void updateRouteLabel(String from, String to, java.util.Date departDate) {
        String routeText = from + " - " + to + ", " + app.getNumberOfPassengers() + " passenger" + 
                          (app.getNumberOfPassengers() > 1 ? "s" : "");
        if (departDate != null) {
            routeText += " (" + departDate.toString() + ")";
        }
        routeLabel.setText(routeText);
    }
    
    private void addFlightCard(Flight flight) throws SQLException {
        Airport depAirport = Airport.load(flight.getDepartureAirportId());
        Airport arrAirport = Airport.load(flight.getArrivalAirportId());
        Aircraft aircraft = Aircraft.load(flight.getAircraftId());
        WeeklySchedule schedule = WeeklySchedule.load(flight.getFlightScheduleId());
        Airline airline = Airline.load(aircraft.getAirlineId());
        
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
        
        JLabel depTimeLabel = new JLabel(schedule.getDepartureTime().toString());
        depTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JLabel connectorLabel = new JLabel(" ─────────── ");
        
        JLabel airlineIconLabel = new JLabel();
        airlineIconLabel.setIcon(createAirlineIcon(airline.getName()));
        
        JLabel connectorLabel2 = new JLabel(" ─────────── ");
        
        // Calculate arrival time (departure + duration)
        String arrTime = calculateArrivalTime(schedule.getDepartureTime(), flight.getDuration());
        JLabel arrTimeLabel = new JLabel(arrTime);
        arrTimeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        timelinePanel.add(depTimeLabel);
        timelinePanel.add(connectorLabel);
        timelinePanel.add(airlineIconLabel);
        timelinePanel.add(connectorLabel2);
        timelinePanel.add(arrTimeLabel);
        
        JPanel airportsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        airportsPanel.setBackground(FlightBookingApp.WHITE);
        
        JLabel depAirportLabel = new JLabel("Airport: " + depAirport.getCode());
        depAirportLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel spacerLabel = new JLabel("                                             ");
        
        String durationText = flight.getDuration() != null ? flight.getDuration() / 60 + "h " + flight.getDuration() % 60 + "m" : "N/A";
        JLabel durationLabel = new JLabel(durationText + " (Nonstop)");
        durationLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        durationLabel.setForeground(Color.GRAY);
        
        JLabel spacerLabel2 = new JLabel("                    ");
        
        JLabel arrAirportLabel = new JLabel("Airport: " + arrAirport.getCode());
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
        
        // Placeholder price calculation
        double price = calculatePrice(flight, app.getSelectedSchedule().getCustomDate());
        JLabel priceValueLabel = new JLabel("Price: $" + String.format("%.2f", price));
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
            app.setSelectedFlight(flight);
            app.setSelectedAircraft(aircraft);
            app.navigateTo("passengers");
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
                    "Airline: " + airline.getName() + "\n" +
                    "From: " + depAirport.getName() + " (" + depAirport.getCode() + ")\n" +
                    "To: " + arrAirport.getName() + " (" + arrAirport.getCode() + ")\n" +
                    "Departure: " + schedule.getDepartureTime() + "\n" +
                    "Arrival: " + arrTime + "\n" +
                    "Duration: " + durationText + "\n" +
                    "Type: Nonstop\n" +
                    "Price: $" + String.format("%.2f", price),
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
    
    private String calculateArrivalTime(Time departureTime, Integer duration) {
        if (duration == null) return "N/A";
        long depMillis = departureTime.getTime();
        long arrMillis = depMillis + (duration * 60 * 1000);
        return new Time(arrMillis).toString();
    }
    
    private double calculatePrice(Flight flight, Date date) {
        // Placeholder: Base price + premium for date proximity
        double basePrice = 200.0;
        if (date != null) {
            long daysUntilFlight = (date.getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
            if (daysUntilFlight < 7) basePrice *= 1.5;
        }
        return basePrice * app.getNumberOfPassengers();
    }
}