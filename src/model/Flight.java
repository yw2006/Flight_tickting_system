package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.*;
public class Flight {
    private int id;
    private int arrivalAirportId;
    private int departureAirportId;
    private String gate;
    private Integer duration;
    private int flightScheduleId;
    private int aircraftId;
    private List<CustomSchedule> customSchedules;
    private List<Payment> payments;

    public Flight(Airport departure, Airport arrival, String gate, Integer duration, WeeklySchedule schedule, Aircraft aircraft) {
        this.departureAirportId = departure.getId();
        this.arrivalAirportId = arrival.getId();
        this.gate = gate;
        this.duration = duration;
        this.flightScheduleId = schedule.getId();
        this.aircraftId = aircraft.getId();
        this.customSchedules = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public void save() throws SQLException {
        String sql = "INSERT INTO flight (arrival_airport_id, departure_airport_id, gate, duration, flight_schedule_id, aircraft_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, arrivalAirportId);
            stmt.setInt(2, departureAirportId);
            stmt.setString(3, gate);
            stmt.setObject(4, duration);
            stmt.setInt(5, flightScheduleId);
            stmt.setInt(6, aircraftId);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static Flight load(int id) throws SQLException {
        String sql = "SELECT * FROM flight WHERE id = ?";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Flight flight = new Flight(
                        Airport.load(rs.getInt("departure_airport_id")),
                        Airport.load(rs.getInt("arrival_airport_id")),
                        rs.getString("gate"),
                        rs.getInt("duration"),
                        WeeklySchedule.load(rs.getInt("flight_schedule_id")),
                        Aircraft.load(rs.getInt("aircraft_id"))
                    );
                    flight.setId(id);
                    return flight;
                }
                throw new SQLException("Flight not found");
            }
        }
    }

    public List<CustomSchedule> getCustomSchedules() throws SQLException {
        if (customSchedules.isEmpty()) {
            String sql = "SELECT id FROM customSchedule WHERE flight_id = ?";
            try (Connection conn = DbConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        customSchedules.add(CustomSchedule.load(rs.getInt("id")));
                    }
                }
            }
        }
        return customSchedules;
    }

    public List<Payment> getPayments() throws SQLException {
        if (payments.isEmpty()) {
            String sql = "SELECT payment_id FROM flight_payment WHERE flight_id = ?";
            try (Connection conn = DbConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        payments.add(Payment.load(rs.getInt("payment_id")));
                    }
                }
            }
        }
        return payments;
    }

    public void addCustomSchedule(CustomSchedule schedule) throws SQLException {
        schedule.save(id);
        customSchedules.add(schedule);
    }

    public void addPayment(Payment payment) throws SQLException {
        payment.save();
        String sql = "INSERT INTO flight_payment (flight_id, payment_id) VALUES (?, ?)";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setInt(2, payment.getId());
            stmt.executeUpdate();
            payments.add(payment);
        }
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getArrivalAirportId() { return arrivalAirportId; }
    public void setArrivalAirportId(int arrivalAirportId) { this.arrivalAirportId = arrivalAirportId; }
    public int getDepartureAirportId() { return departureAirportId; }
    public void setDepartureAirportId(int departureAirportId) { this.departureAirportId = departureAirportId; }
    public String getGate() { return gate; }
    public void setGate(String gate) { this.gate = gate; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public int getFlightScheduleId() { return flightScheduleId; }
    public void setFlightScheduleId(int flightScheduleId) { this.flightScheduleId = flightScheduleId; }
    public int getAircraftId() { return aircraftId; }
    public void setAircraftId(int aircraftId) { this.aircraftId = aircraftId; }
}