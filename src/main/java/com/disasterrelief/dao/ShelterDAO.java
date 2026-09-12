package com.disasterrelief.dao;

import com.disasterrelief.model.Shelter;
import com.disasterrelief.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShelterDAO {

    public List<Shelter> findAll() throws SQLException {
        List<Shelter> list = new ArrayList<>();
        String sql = "SELECT s.*, d.name AS disaster_name, u.name AS manager_name " +
                     "FROM shelters s " +
                     "LEFT JOIN disasters d ON s.disaster_id = d.disaster_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "ORDER BY s.shelter_id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Shelter findById(int id) throws SQLException {
        String sql = "SELECT s.*, d.name AS disaster_name, u.name AS manager_name " +
                     "FROM shelters s " +
                     "LEFT JOIN disasters d ON s.disaster_id = d.disaster_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.shelter_id = ?";
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

    public Shelter findByUserId(int userId) throws SQLException {
        String sql = "SELECT s.*, d.name AS disaster_name, u.name AS manager_name " +
                     "FROM shelters s " +
                     "LEFT JOIN disasters d ON s.disaster_id = d.disaster_id " +
                     "LEFT JOIN users u ON s.user_id = u.user_id " +
                     "WHERE s.user_id = ?";
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

    public int create(Shelter shelter) throws SQLException {
        String sql = "INSERT INTO shelters (disaster_id, user_id, name, location, capacity, current_population, contact, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (shelter.getDisasterId() != null && shelter.getDisasterId() > 0) {
                ps.setInt(1, shelter.getDisasterId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            if (shelter.getUserId() != null && shelter.getUserId() > 0) {
                ps.setInt(2, shelter.getUserId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, shelter.getName());
            ps.setString(4, shelter.getLocation());
            ps.setInt(5, shelter.getCapacity());
            ps.setInt(6, shelter.getCurrentPopulation());
            ps.setString(7, shelter.getContact());
            ps.setString(8, shelter.getStatus());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    shelter.setShelterId(gk.getInt(1));
                    return shelter.getShelterId();
                }
            }
        }
        return -1;
    }

    public boolean update(Shelter shelter) throws SQLException {
        String sql = "UPDATE shelters SET disaster_id=?, user_id=?, name=?, location=?, capacity=?, current_population=?, contact=?, status=? WHERE shelter_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (shelter.getDisasterId() != null && shelter.getDisasterId() > 0) {
                ps.setInt(1, shelter.getDisasterId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            if (shelter.getUserId() != null && shelter.getUserId() > 0) {
                ps.setInt(2, shelter.getUserId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, shelter.getName());
            ps.setString(4, shelter.getLocation());
            ps.setInt(5, shelter.getCapacity());
            ps.setInt(6, shelter.getCurrentPopulation());
            ps.setString(7, shelter.getContact());
            ps.setString(8, shelter.getStatus());
            ps.setInt(9, shelter.getShelterId());
            return ps.executeUpdate() > 0;
        }
    }

    private Shelter mapRow(ResultSet rs) throws SQLException {
        Shelter s = new Shelter(
                rs.getInt("shelter_id"),
                (Integer) rs.getObject("disaster_id"),
                (Integer) rs.getObject("user_id"),
                rs.getString("name"),
                rs.getString("location"),
                rs.getInt("capacity"),
                rs.getInt("current_population"),
                rs.getString("contact"),
                rs.getString("status")
        );
        s.setDisasterName(rs.getString("disaster_name"));
        s.setManagerName(rs.getString("manager_name"));
        s.setCreatedAt(rs.getTimestamp("created_at"));
        return s;
    }
}
