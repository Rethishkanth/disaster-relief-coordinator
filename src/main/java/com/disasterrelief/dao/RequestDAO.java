package com.disasterrelief.dao;

import com.disasterrelief.model.ResourceRequest;
import com.disasterrelief.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    public List<ResourceRequest> findAll() throws SQLException {
        List<ResourceRequest> list = new ArrayList<>();
        String sql = "SELECT req.*, s.name AS shelter_name, s.location AS shelter_location " +
                     "FROM resource_requests req " +
                     "JOIN shelters s ON req.shelter_id = s.shelter_id " +
                     "ORDER BY req.priority_score DESC, req.requested_date ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<ResourceRequest> findPending() throws SQLException {
        List<ResourceRequest> list = new ArrayList<>();
        String sql = "SELECT req.*, s.name AS shelter_name, s.location AS shelter_location " +
                     "FROM resource_requests req " +
                     "JOIN shelters s ON req.shelter_id = s.shelter_id " +
                     "WHERE req.status = 'PENDING' " +
                     "ORDER BY req.priority_score DESC, req.requested_date ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public ResourceRequest findById(int id) throws SQLException {
        String sql = "SELECT req.*, s.name AS shelter_name, s.location AS shelter_location " +
                     "FROM resource_requests req " +
                     "JOIN shelters s ON req.shelter_id = s.shelter_id " +
                     "WHERE req.request_id = ?";
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

    public List<ResourceRequest> findByShelterId(int shelterId) throws SQLException {
        List<ResourceRequest> list = new ArrayList<>();
        String sql = "SELECT req.*, s.name AS shelter_name, s.location AS shelter_location " +
                     "FROM resource_requests req " +
                     "JOIN shelters s ON req.shelter_id = s.shelter_id " +
                     "WHERE req.shelter_id = ? " +
                     "ORDER BY req.request_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, shelterId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public int create(ResourceRequest req) throws SQLException {
        String sql = "INSERT INTO resource_requests (shelter_id, resource_type, quantity, unit, urgency, people_affected, priority_score, status, notes, required_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, req.getShelterId());
            ps.setString(2, req.getResourceType());
            ps.setInt(3, req.getQuantity());
            ps.setString(4, req.getUnit());
            ps.setString(5, req.getUrgency());
            ps.setInt(6, req.getPeopleAffected());
            ps.setDouble(7, req.getPriorityScore());
            ps.setString(8, req.getStatus());
            ps.setString(9, req.getNotes());
            ps.setString(10, req.getRequiredDate());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    req.setRequestId(gk.getInt(1));
                    return req.getRequestId();
                }
            }
        }
        return -1;
    }

    public boolean updateStatus(int requestId, String status) throws SQLException {
        String sql = "UPDATE resource_requests SET status = ? WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updatePriorityScore(int requestId, double score) throws SQLException {
        String sql = "UPDATE resource_requests SET priority_score = ? WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, score);
            ps.setInt(2, requestId);
            return ps.executeUpdate() > 0;
        }
    }

    private ResourceRequest mapRow(ResultSet rs) throws SQLException {
        ResourceRequest req = new ResourceRequest(
                rs.getInt("request_id"),
                rs.getInt("shelter_id"),
                rs.getString("resource_type"),
                rs.getInt("quantity"),
                rs.getString("unit"),
                rs.getString("urgency"),
                rs.getInt("people_affected"),
                rs.getDouble("priority_score"),
                rs.getString("status"),
                rs.getString("notes"),
                rs.getTimestamp("requested_date"),
                rs.getString("required_date")
        );
        req.setShelterName(rs.getString("shelter_name"));
        req.setShelterLocation(rs.getString("shelter_location"));
        return req;
    }
}
