package com.disasterrelief.dao;

import com.disasterrelief.model.Disaster;
import com.disasterrelief.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DisasterDAO {

    public List<Disaster> findAll() throws SQLException {
        List<Disaster> list = new ArrayList<>();
        String sql = "SELECT * FROM disasters ORDER BY disaster_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Disaster findById(int id) throws SQLException {
        String sql = "SELECT * FROM disasters WHERE disaster_id = ?";
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

    public int create(Disaster disaster) throws SQLException {
        String sql = "INSERT INTO disasters (name, disaster_type, location, severity, status, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, disaster.getName());
            ps.setString(2, disaster.getDisasterType());
            ps.setString(3, disaster.getLocation());
            ps.setString(4, disaster.getSeverity());
            ps.setString(5, disaster.getStatus());
            ps.setString(6, disaster.getDescription());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    disaster.setDisasterId(gk.getInt(1));
                    return disaster.getDisasterId();
                }
            }
        }
        return -1;
    }

    public boolean update(Disaster disaster) throws SQLException {
        String sql = "UPDATE disasters SET name=?, disaster_type=?, location=?, severity=?, status=?, description=? WHERE disaster_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, disaster.getName());
            ps.setString(2, disaster.getDisasterType());
            ps.setString(3, disaster.getLocation());
            ps.setString(4, disaster.getSeverity());
            ps.setString(5, disaster.getStatus());
            ps.setString(6, disaster.getDescription());
            ps.setInt(7, disaster.getDisasterId());
            return ps.executeUpdate() > 0;
        }
    }

    private Disaster mapRow(ResultSet rs) throws SQLException {
        Disaster d = new Disaster(
                rs.getInt("disaster_id"),
                rs.getString("name"),
                rs.getString("disaster_type"),
                rs.getString("location"),
                rs.getString("severity"),
                rs.getString("status"),
                rs.getString("description")
        );
        d.setCreatedAt(rs.getTimestamp("created_at"));
        return d;
    }
}
