package com.disasterrelief.dao;

import com.disasterrelief.model.Resource;
import com.disasterrelief.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    public List<Resource> findAll() throws SQLException {
        List<Resource> list = new ArrayList<>();
        String sql = "SELECT r.*, o.org_name FROM resources r " +
                     "LEFT JOIN organizations o ON r.organization_id = o.organization_id " +
                     "ORDER BY r.resource_id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Resource findById(int id) throws SQLException {
        String sql = "SELECT r.*, o.org_name FROM resources r " +
                     "LEFT JOIN organizations o ON r.organization_id = o.organization_id " +
                     "WHERE r.resource_id = ?";
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

    public List<Resource> findByType(String resourceType) throws SQLException {
        List<Resource> list = new ArrayList<>();
        String sql = "SELECT r.*, o.org_name FROM resources r " +
                     "LEFT JOIN organizations o ON r.organization_id = o.organization_id " +
                     "WHERE r.resource_type = ? AND r.quantity > 0 " +
                     "ORDER BY r.quantity DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resourceType);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public int create(Resource resource) throws SQLException {
        String sql = "INSERT INTO resources (organization_id, resource_name, resource_type, quantity, unit, expiry_date, availability_status, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (resource.getOrganizationId() != null && resource.getOrganizationId() > 0) {
                ps.setInt(1, resource.getOrganizationId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, resource.getResourceName());
            ps.setString(3, resource.getResourceType());
            ps.setInt(4, resource.getQuantity());
            ps.setString(5, resource.getUnit());
            ps.setString(6, resource.getExpiryDate());
            ps.setString(7, resource.getAvailabilityStatus());
            ps.setString(8, resource.getDescription());
            ps.executeUpdate();
            try (ResultSet gk = ps.getGeneratedKeys()) {
                if (gk.next()) {
                    resource.setResourceId(gk.getInt(1));
                    return resource.getResourceId();
                }
            }
        }
        return -1;
    }

    public boolean update(Resource resource) throws SQLException {
        String sql = "UPDATE resources SET organization_id=?, resource_name=?, resource_type=?, quantity=?, unit=?, expiry_date=?, availability_status=?, description=? WHERE resource_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (resource.getOrganizationId() != null && resource.getOrganizationId() > 0) {
                ps.setInt(1, resource.getOrganizationId());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, resource.getResourceName());
            ps.setString(3, resource.getResourceType());
            ps.setInt(4, resource.getQuantity());
            ps.setString(5, resource.getUnit());
            ps.setString(6, resource.getExpiryDate());
            ps.setString(7, resource.getAvailabilityStatus());
            ps.setString(8, resource.getDescription());
            ps.setInt(9, resource.getResourceId());
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Atomically deducts quantity from a resource inventory stock.
     * Prevents negative inventory.
     */
    public boolean deductQuantity(int resourceId, int amount) throws SQLException {
        String sql = "UPDATE resources SET quantity = quantity - ?, " +
                     "availability_status = CASE WHEN (quantity - ?) <= 0 THEN 'DEPLETED' WHEN (quantity - ?) < 50 THEN 'LOW_STOCK' ELSE 'AVAILABLE' END " +
                     "WHERE resource_id = ? AND quantity >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, amount);
            ps.setInt(3, amount);
            ps.setInt(4, resourceId);
            ps.setInt(5, amount);
            return ps.executeUpdate() > 0;
        }
    }

    private Resource mapRow(ResultSet rs) throws SQLException {
        Resource r = new Resource(
                rs.getInt("resource_id"),
                (Integer) rs.getObject("organization_id"),
                rs.getString("resource_name"),
                rs.getString("resource_type"),
                rs.getInt("quantity"),
                rs.getString("unit"),
                rs.getString("expiry_date"),
                rs.getString("availability_status"),
                rs.getString("description")
        );
        r.setOrgName(rs.getString("org_name"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}
