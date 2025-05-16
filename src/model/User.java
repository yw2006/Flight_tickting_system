package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import util.*;
public class User {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String password;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer age;
    private int roleId;
    private List<FlightReservation> reservations;

    public User(String username, String email, String phone, String password, Integer age, Role role) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password; // In production, hash the password
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.age = age;
        this.roleId = role.getId();
        this.reservations = new ArrayList<>();
    }

    public void save() throws SQLException {
        String sql = "INSERT INTO user (username, email, phone, password, created_at, age, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, password);
            stmt.setTimestamp(5, createdAt);
            stmt.setObject(6, age);
            stmt.setInt(7, roleId);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static User loadWithId(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        rs.getInt("age"),
                        Role.load(rs.getInt("role_id"))
                    );
                    user.setId(id);
                    user.setCreatedAt(rs.getTimestamp("created_at"));
                    user.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return user;
                }
                throw new SQLException("User not found");
            }
        }
    }
    public static User loadWithEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String username = rs.getString("username");
                    String rsEmail = rs.getString("email");
                    String phone = rs.getString("phone");
                    String password = rs.getString("password");
                    int age = rs.getInt("age");
                    int roleId = rs.getInt("role_id");
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    Timestamp updatedAt = rs.getTimestamp("updated_at");
                    
                    User user = new User(
                        username,
                        rsEmail,
                        phone,
                        password,
                        age,
                        Role.load(roleId) 
                    );
                    user.setId(userId);
                    user.setCreatedAt(createdAt);
                    user.setUpdatedAt(updatedAt);
                    return user;
                }
                throw new SQLException("User not found");
            }
        }
    }

    public void register() throws SQLException {
        save();
    }

    public boolean resetPassword(String newPassword) throws SQLException {
        this.password = newPassword; // In production, hash the password
        String sql = "UPDATE user SET password = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<FlightReservation> getReservations() throws SQLException {
        if (reservations.isEmpty()) {
            String sql = "SELECT id FROM flightReservation WHERE user_id = ?";
            try (Connection conn = DbConnection.getInstance();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        reservations.add(FlightReservation.load(rs.getInt("id")));
                    }
                }
            }
        }
        return reservations;
    }

    public void addReservation(FlightReservation reservation) throws SQLException {
        reservation.setUserId(id);
        reservation.save();
        reservations.add(reservation);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
}