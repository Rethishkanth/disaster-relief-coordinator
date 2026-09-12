package com.disasterrelief.dao;

import com.disasterrelief.model.Volunteer;
import com.disasterrelief.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDAO {

    public List<Volunteer> findAll() throws SQLException {
        List<Volunteer> list = new ArrayList<>();
        String sql = "SELECT v.*, u.email, u.password, u.is_active FROM volunteers v " +
                     "LEFT JOIN users u ON v.user_id = u.user_id " +
                     "ORDER BY v.volunteer_id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Volunteer> findAvailable() throws SQLException {
        List<Volunteer> list = new ArrayList<>();
        String sql = "SELECT v.*, u.email, u.password, u.is_active FROM volunteers v " +
                     "LEFT JOIN users u ON v.user_id = u.user_id " +
                     "WHERE v.availability_status = 'AVAILABLE' " +
                     "ORDER BY v.experience_years DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Volunteer findById(int id) throws SQLException {
        String sql = "SELECT v.*, u.email, u.password, u.is_active FROM volunteers v " +
                     "LEFT JOIN users u ON v.user_id = u.user_id " +
                     "WHERE v.volunteer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public Volunteer findByUserId(int userId) throws SQLException {
        String sql = "SELECT v.*, u.email, u.password, u.is_active FROM volunteers v " +
                     "LEFT JOIN users u ON v.user_id = u.user_id " +
                     "WHERE v.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public int create(Volunteer v) throws SQLException {
        String sql = "INSERT INTO volunteers (user_id, name, skill, experience_years, availability_status, contact, location) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (v.getUserId() > 0) {
                ps.setInt(1, v.getUserId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, v.getName());
            ps.setString(3, v.getSkill());
            ps.setInt(4, v.getExperienceYears());
            ps.setString(5, v.getAvailabilityStatus());
            ps.setString(6, v.getContact());
            ps.setString(7, v.getLocation());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    v.setVolunteerId(gk.getInt(1));
                    return v.getVolunteerId();
                }
            }
        }
        return -1;
    }

    public boolean updateStatus(int volunteerId, String status) throws SQLException {
        String sql = "UPDATE volunteers SET availability_status = ? WHERE volunteer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, volunteerId);
            return ps.executeUpdate() > 0;
        }
    }

    private Volunteer mapRow(ResultSet rs) throws SQLException {
        int userId = rs.getInt("user_id");
        String email = rs.getString("email");
        String password = rs.getString("password");
        boolean active = rs.getBoolean("is_active");
        if (email == null) email = "volunteer" + rs.getInt("volunteer_id") + "@relief.org";

        return new Volunteer(
                userId,
                rs.getString("name"),
                email,
                password,
                rs.getString("contact"),
                active,
                rs.getInt("volunteer_id"),
                rs.getString("skill"),
                rs.getInt("experience_years"),
                rs.getString("availability_status"),
                rs.getString("location")
        );
    }
}
