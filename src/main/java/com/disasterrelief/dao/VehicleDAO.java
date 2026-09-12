package com.disasterrelief.dao;

import com.disasterrelief.model.Vehicle;
import com.disasterrelief.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public List<Vehicle> findAll() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY vehicle_id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Vehicle> findAvailable() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE availability_status = 'AVAILABLE' ORDER BY capacity_kg DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Vehicle findById(int id) throws SQLException {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
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

    public int create(Vehicle vehicle) throws SQLException {
        String sql = "INSERT INTO vehicles (vehicle_number, vehicle_type, capacity_kg, driver_name, contact, availability_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, vehicle.getVehicleNumber());
            ps.setString(2, vehicle.getVehicleType());
            ps.setInt(3, vehicle.getCapacityKg());
            ps.setString(4, vehicle.getDriverName());
            ps.setString(5, vehicle.getContact());
            ps.setString(6, vehicle.getAvailabilityStatus());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    vehicle.setVehicleId(gk.getInt(1));
                    return vehicle.getVehicleId();
                }
            }
        }
        return -1;
    }

    public boolean updateStatus(int vehicleId, String status) throws SQLException {
        String sql = "UPDATE vehicles SET availability_status = ? WHERE vehicle_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, vehicleId);
            return ps.executeUpdate() > 0;
        }
    }

    private Vehicle mapRow(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("vehicle_id"),
                rs.getString("vehicle_number"),
                rs.getString("vehicle_type"),
                rs.getInt("capacity_kg"),
                rs.getString("driver_name"),
                rs.getString("contact"),
                rs.getString("availability_status")
        );
    }
}
