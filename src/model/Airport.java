package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.*;
public class Airport {
    private int id;
    private int countryId;
    private String code;
    private String name;
    private String address;
    private String status;
    private List<Flight> departures;
    private List<Flight> arrivals;

    public Airport(String code, String name, String address, String status) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.status = status;
        this.departures = new ArrayList<>();
        this.arrivals = new ArrayList<>();
    }

    public void save() throws SQLException {
        String sql = "INSERT INTO airport (country_id, code, name, address, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, countryId);
            stmt.setString(2, code);
            stmt.setString(3, name);
            stmt.setString(4, address);
            stmt.setString(5, status);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static Airport load(int id) throws SQLException {
        String sql = "SELECT * FROM airport WHERE id = ?";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Airport airport = new Airport(
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("status")
                    );
                    airport.setId(id);
                    airport.setCountryId(rs.getInt("country_id"));
                    return airport;
                }
                throw new SQLException("Airport not found");
            }
        }
    }

    public List<Flight> getDepartures() throws SQLException {
        if (departures.isEmpty()) {
            String sql = "SELECT id FROM flight WHERE departure_airport_id = ?";
            try (Connection conn = DbConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        departures.add(Flight.load(rs.getInt("id")));
                    }
                }
            }
        }
        return departures;
    }

    public List<Flight> getArrivals() throws SQLException {
        if (arrivals.isEmpty()) {
            String sql = "SELECT id FROM flight WHERE arrival_airport_id = ?";
            try (Connection conn = DbConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        arrivals.add(Flight.load(rs.getInt("id")));
                    }
                }
            }
        }
        return arrivals;
    }

    public List<Flight> getFlights() throws SQLException {
        List<Flight> allFlights = new ArrayList<>();
        allFlights.addAll(getDepartures());
        allFlights.addAll(getArrivals());
        return allFlights;
    }

    public void addDeparture(Flight flight) throws SQLException {
        flight.setDepartureAirportId(id);
        flight.save();
        departures.add(flight);
    }

    public void addArrival(Flight flight) throws SQLException {
        flight.setArrivalAirportId(id);
        flight.save();
        arrivals.add(flight);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCountryId() { return countryId; }
    public void setCountryId(int countryId) { this.countryId = countryId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}