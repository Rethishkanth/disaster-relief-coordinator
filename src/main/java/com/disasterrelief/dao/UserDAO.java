package com.disasterrelief.dao;

import com.disasterrelief.model.Coordinator;
import com.disasterrelief.model.Organization;
import com.disasterrelief.model.ShelterUser;
import com.disasterrelief.model.User;
import com.disasterrelief.model.Volunteer;
import com.disasterrelief.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User Data Access Object.
 * Demonstrates:
 * - Pure JDBC with parameterized PreparedStatement
 * - Polymorphic instantiation of specialized User subclasses
 */
public class UserDAO {

    public User authenticate(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND is_active = TRUE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email != null ? email.trim() : "");
            ps.setString(2, password != null ? password.trim() : "");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPolymorphicUser(rs, conn);
                }
            }
        }
        return null;
    }

    public User findById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPolymorphicUser(rs, conn);
                }
            }
        }
        return null;
    }

    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRowToPolymorphicUser(rs, conn));
            }
        }
        return list;
    }

    public int create(User user) throws SQLException {
        String sql = "INSERT INTO users (name, email, password, role, contact, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getContact());
            ps.setBoolean(6, user.isActive());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    user.setUserId(gk.getInt(1));
                    return user.getUserId();
                }
            }
        }
        return -1;
    }

    /**
     * Polymorphic User Instantiation based on role.
     */
    private User mapRowToPolymorphicUser(ResultSet rs, Connection conn) throws SQLException {
        int userId = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String role = rs.getString("role");
        String contact = rs.getString("contact");
        boolean active = rs.getBoolean("is_active");

        if ("COORDINATOR".equalsIgnoreCase(role)) {
            return new Coordinator(userId, name, email, password, contact, active);
        } else if ("VOLUNTEER".equalsIgnoreCase(role)) {
            Volunteer vol = new Volunteer(userId, name, email, password, contact, active, 0, "FIRST_AID", 1, "AVAILABLE", "");
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM volunteers WHERE user_id = ?")) {
                ps.setInt(1, userId);
                try (ResultSet vrs = ps.executeQuery()) {
                    if (vrs.next()) {
                        vol.setVolunteerId(vrs.getInt("volunteer_id"));
                        vol.setSkill(vrs.getString("skill"));
                        vol.setExperienceYears(vrs.getInt("experience_years"));
                        vol.setAvailabilityStatus(vrs.getString("availability_status"));
                        vol.setLocation(vrs.getString("location"));
                    }
                }
            }
            return vol;
        } else if ("ORGANIZATION".equalsIgnoreCase(role)) {
            Organization org = new Organization(userId, name, email, password, contact, active, 0, name, "NGO", name, contact, "");
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM organizations WHERE user_id = ?")) {
                ps.setInt(1, userId);
                try (ResultSet ors = ps.executeQuery()) {
                    if (ors.next()) {
                        org.setOrganizationId(ors.getInt("organization_id"));
                        org.setOrgName(ors.getString("org_name"));
                        org.setOrgType(ors.getString("org_type"));
                        org.setContactPerson(ors.getString("contact_person"));
                        org.setPhone(ors.getString("phone"));
                        org.setAddress(ors.getString("address"));
                    }
                }
            }
            return org;
        } else if ("SHELTER".equalsIgnoreCase(role)) {
            ShelterUser su = new ShelterUser(userId, name, email, password, contact, active, 0, name);
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM shelters WHERE user_id = ?")) {
                ps.setInt(1, userId);
                try (ResultSet srs = ps.executeQuery()) {
                    if (srs.next()) {
                        su.setShelterId(srs.getInt("shelter_id"));
                        su.setShelterName(srs.getString("name"));
                    }
                }
            }
            return su;
        }

        // Fallback generic Coordinator
        return new Coordinator(userId, name, email, password, contact, active);
    }
}
