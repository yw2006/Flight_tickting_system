package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.*;
public class Aircraft {
    private int id;
    private int airlineId;
    private String model;
    private Integer manufacturingYear;
    private List<Seat> seats;

    public Aircraft(String model, Integer manufacturingYear) {
        this.model = model;
        this.manufacturingYear = manufacturingYear;
        this.seats = new ArrayList<>();
    }

    public void save(int airlineId) throws SQLException {
        this.airlineId = airlineId;
        String sql = "INSERT INTO aircraft (airline_id, modal, manufacturing_year) VALUES (?, ?, ?)";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, airlineId);
            stmt.setString(2, model);
            stmt.setObject(3, manufacturingYear);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static Aircraft load(int id) throws SQLException {
        String sql = "SELECT * FROM aircraft WHERE id = ?";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Aircraft aircraft = new Aircraft(
                        rs.getString("modal"),
                        rs.getInt("manufacturing_year")
                    );
                    aircraft.setId(id);
                    aircraft.setAirlineId(rs.getInt("airline_id"));
                    return aircraft;
                }
                throw new SQLException("Aircraft not found");
            }
        }
    }

    public List<Seat> getSeats() throws SQLException {
        if (seats.isEmpty()) {
            String sql = "SELECT id FROM seat WHERE aircraft_id = ?";
            try (Connection conn = DbConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        seats.add(Seat.load(rs.getInt("id")));
                    }
                }
            }
        }
        return seats;
    }

    public void addSeat(Seat seat) throws SQLException {
        seat.save(id);
        seats.add(seat);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAirlineId() { return airlineId; }
    public void setAirlineId(int airlineId) { this.airlineId = airlineId; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public Integer getManufacturingYear() { return manufacturingYear; }
    public void setManufacturingYear(Integer manufacturingYear) { this.manufacturingYear = manufacturingYear; }
}