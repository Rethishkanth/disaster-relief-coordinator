package com.disasterrelief.service;

import com.disasterrelief.dao.DisasterDAO;
import com.disasterrelief.exception.DatabaseException;
import com.disasterrelief.exception.InvalidRequestException;
import com.disasterrelief.model.Disaster;
import com.disasterrelief.util.FileLogger;

import java.sql.SQLException;
import java.util.List;

public class DisasterService {

    private final DisasterDAO disasterDAO = new DisasterDAO();

    public List<Disaster> getAllDisasters() throws DatabaseException {
        try {
            return disasterDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to retrieve disasters: " + e.getMessage(), e);
        }
    }

    public Disaster getDisasterById(int id) throws DatabaseException {
        try {
            return disasterDAO.findById(id);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find disaster: " + e.getMessage(), e);
        }
    }

    public int registerDisaster(Disaster disaster) throws InvalidRequestException, DatabaseException {
        if (disaster.getName() == null || disaster.getName().trim().isEmpty()) {
            throw new InvalidRequestException("name", "Disaster name is required.");
        }
        if (disaster.getLocation() == null || disaster.getLocation().trim().isEmpty()) {
            throw new InvalidRequestException("location", "Disaster location is required.");
        }

        try {
            int id = disasterDAO.create(disaster);
            FileLogger.logSystem("WARN", "DISASTER", "New disaster registered: " + disaster.getName() + " (" + disaster.getSeverity() + ")");
            return id;
        } catch (SQLException e) {
            throw new DatabaseException("Could not register disaster: " + e.getMessage(), e);
        }
    }

    public boolean updateDisaster(Disaster disaster) throws DatabaseException {
        try {
            return disasterDAO.update(disaster);
        } catch (SQLException e) {
            throw new DatabaseException("Failed updating disaster: " + e.getMessage(), e);
        }
    }
}
