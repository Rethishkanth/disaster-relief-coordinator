package com.disasterrelief.service;

import com.disasterrelief.dao.ResourceDAO;
import com.disasterrelief.exception.DatabaseException;
import com.disasterrelief.exception.InvalidRequestException;
import com.disasterrelief.model.Resource;
import com.disasterrelief.util.FileLogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resource Service managing supplies inventory.
 * Demonstrates:
 * - Collections: Uses HashMap for rapid categorization and lookups by resource type
 */
public class ResourceService {

    private final ResourceDAO resourceDAO = new ResourceDAO();

    public List<Resource> getAllResources() throws DatabaseException {
        try {
            return resourceDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch resources: " + e.getMessage(), e);
        }
    }

    public Resource getResourceById(int id) throws DatabaseException {
        try {
            return resourceDAO.findById(id);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch resource #" + id + ": " + e.getMessage(), e);
        }
    }

    public List<Resource> getAvailableResourcesByType(String resourceType) throws DatabaseException {
        try {
            return resourceDAO.findByType(resourceType);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query available resources: " + e.getMessage(), e);
        }
    }

    /**
     * Demonstrates Collections: HashMap
     * Organizes resources into an in-memory Map categorized by Resource Type for O(1) group lookup.
     */
    public Map<String, List<Resource>> getResourcesGroupedByType() throws DatabaseException {
        List<Resource> all = getAllResources();
        Map<String, List<Resource>> grouped = new HashMap<>();

        for (Resource r : all) {
            String type = r.getResourceType();
            grouped.computeIfAbsent(type, k -> new ArrayList<>()).add(r);
        }

        return grouped;
    }

    public int registerResource(Resource resource) throws InvalidRequestException, DatabaseException {
        if (resource.getResourceName() == null || resource.getResourceName().trim().isEmpty()) {
            throw new InvalidRequestException("resourceName", "Resource name is required.");
        }
        if (resource.getQuantity() <= 0) {
            throw new InvalidRequestException("quantity", "Quantity must be greater than zero.");
        }
        if (resource.getResourceType() == null || resource.getResourceType().trim().isEmpty()) {
            throw new InvalidRequestException("resourceType", "Resource type is required.");
        }

        try {
            int id = resourceDAO.create(resource);
            FileLogger.logSystem("INFO", "RESOURCE",
                    "New resource registered: " + resource.getResourceName() + " (" + resource.getQuantity() + " " + resource.getUnit() + ")");
            return id;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to register resource: " + e.getMessage(), e);
        }
    }

    public boolean updateResource(Resource resource) throws DatabaseException {
        try {
            return resourceDAO.update(resource);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update resource: " + e.getMessage(), e);
        }
    }
}
