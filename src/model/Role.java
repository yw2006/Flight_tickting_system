package model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import util.*;
public class Role {
    private int id;
    private String roleName;
    private String description;

    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public void save() throws SQLException {
        String sql = "INSERT INTO role (role_name, description) VALUES (?, ?)";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, roleName);
            stmt.setString(2, description);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getInt(1);
                }
            }
        }
    }

    public static Role load(int id) throws SQLException {
        String sql = "SELECT * FROM role WHERE id = ?";
        try (Connection conn = DbConnection.getInstance();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role(rs.getString("role_name"), rs.getString("description"));
                    role.setId(id);
                    return role;
                }
                throw new SQLException("Role not found");
            }
        }
    }
public static ArrayList<Role> loadAll() throws SQLException {
    String sql = "SELECT * FROM role";
    ArrayList<Role> roles = new ArrayList<>();
    
    try (Connection conn = DbConnection.getInstance();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Role role = new Role(
                rs.getString("role_name"), 
                rs.getString("description")
            );
            role.setId(rs.getInt("id"));
            roles.add(role);
        }
        
        if (roles.isEmpty()) {
            throw new SQLException("No roles found in database");
        }
        
        return roles;
    }
}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
@Override
public String toString() {
    return roleName; // Assuming "roleName" is the property you want to display in the combo box
}
}