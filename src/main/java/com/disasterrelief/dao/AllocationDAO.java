package com.disasterrelief.dao;

import com.disasterrelief.model.Allocation;
import com.disasterrelief.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AllocationDAO {

    public List<Allocation> findAll() throws SQLException {
        List<Allocation> list = new ArrayList<>();
        String sql = "SELECT a.*, r.resource_name, r.resource_type, r.unit, " +
                     "v.name AS volunteer_name, veh.vehicle_number, veh.vehicle_type, " +
                     "s.name AS shelter_name, s.location AS shelter_location " +
                     "FROM allocations a " +
                     "JOIN resources r ON a.resource_id = r.resource_id " +
                     "JOIN resource_requests req ON a.request_id = req.request_id " +
                     "JOIN shelters s ON req.shelter_id = s.shelter_id " +
                     "LEFT JOIN volunteers v ON a.volunteer_id = v.volunteer_id " +
                     "LEFT JOIN vehicles veh ON a.vehicle_id = veh.vehicle_id " +
                     "ORDER BY a.allocation_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Allocation findById(int id) throws SQLException {
        String sql = "SELECT a.*, r.resource_name, r.resource_type, r.unit, " +
                     "v.name AS volunteer_name, veh.vehicle_number, veh.vehicle_type, " +
                     "s.name AS shelter_name, s.location AS shelter_location " +
                     "FROM allocations a " +
                     "JOIN resources r ON a.resource_id = r.resource_id " +
                     "JOIN resource_requests req ON a.request_id = req.request_id " +
                     "JOIN shelters s ON req.shelter_id = s.shelter_id " +
                     "LEFT JOIN volunteers v ON a.volunteer_id = v.volunteer_id " +
                     "LEFT JOIN vehicles veh ON a.vehicle_id = veh.vehicle_id " +
                     "WHERE a.allocation_id = ?";
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

    public List<Allocation> findByVolunteerId(int volunteerId) throws SQLException {
        List<Allocation> list = new ArrayList<>();
        String sql = "SELECT a.*, r.resource_name, r.resource_type, r.unit, " +
                     "v.name AS volunteer_name, veh.vehicle_number, veh.vehicle_type, " +
                     "s.name AS shelter_name, s.location AS shelter_location " +
                     "FROM allocations a " +
                     "JOIN resources r ON a.resource_id = r.resource_id " +
                     "JOIN resource_requests req ON a.request_id = req.request_id " +
                     "JOIN shelters s ON req.shelter_id = s.shelter_id " +
                     "LEFT JOIN volunteers v ON a.volunteer_id = v.volunteer_id " +
                     "LEFT JOIN vehicles veh ON a.vehicle_id = veh.vehicle_id " +
                     "WHERE a.volunteer_id = ? " +
                     "ORDER BY a.allocation_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, volunteerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public int create(Allocation alloc) throws SQLException {
        String sql = "INSERT INTO allocations (request_id, resource_id, allocated_quantity, volunteer_id, vehicle_id, status, tracking_notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, alloc.getRequestId());
            ps.setInt(2, alloc.getResourceId());
            ps.setInt(3, alloc.getAllocatedQuantity());
            if (alloc.getVolunteerId() != null && alloc.getVolunteerId() > 0) {
                ps.setInt(4, alloc.getVolunteerId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            if (alloc.getVehicleId() != null && alloc.getVehicleId() > 0) {
                ps.setInt(5, alloc.getVehicleId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setString(6, alloc.getStatus());
            ps.setString(7, alloc.getTrackingNotes());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    alloc.setAllocationId(gk.getInt(1));
                    return alloc.getAllocationId();
                }
            }
        }
        return -1;
    }

    public boolean updateStatus(Allocation alloc) throws SQLException {
        String sql = "UPDATE allocations SET status = ?, dispatch_date = ?, delivery_date = ?, tracking_notes = ? WHERE allocation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, alloc.getStatus());
            ps.setTimestamp(2, alloc.getDispatchDate());
            ps.setTimestamp(3, alloc.getDeliveryDate());
            ps.setString(4, alloc.getTrackingNotes());
            ps.setInt(5, alloc.getAllocationId());
            return ps.executeUpdate() > 0;
        }
    }

    private Allocation mapRow(ResultSet rs) throws SQLException {
        Allocation a = new Allocation(
                rs.getInt("allocation_id"),
                rs.getInt("request_id"),
                rs.getInt("resource_id"),
                rs.getInt("allocated_quantity"),
                (Integer) rs.getObject("volunteer_id"),
                (Integer) rs.getObject("vehicle_id"),
                rs.getTimestamp("allocation_date"),
                rs.getTimestamp("dispatch_date"),
                rs.getTimestamp("delivery_date"),
                rs.getString("status"),
                rs.getString("tracking_notes")
        );
        a.setResourceName(rs.getString("resource_name"));
        a.setResourceType(rs.getString("resource_type"));
        a.setUnit(rs.getString("unit"));
        a.setVolunteerName(rs.getString("volunteer_name"));
        a.setVehicleNumber(rs.getString("vehicle_number"));
        a.setVehicleType(rs.getString("vehicle_type"));
        a.setShelterName(rs.getString("shelter_name"));
        a.setShelterLocation(rs.getString("shelter_location"));
        return a;
    }
}
