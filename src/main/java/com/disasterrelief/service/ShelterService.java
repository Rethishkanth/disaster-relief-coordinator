package com.disasterrelief.service;

import com.disasterrelief.dao.ShelterDAO;
import com.disasterrelief.exception.DatabaseException;
import com.disasterrelief.exception.InvalidRequestException;
import com.disasterrelief.exception.ShelterNotFoundException;
import com.disasterrelief.model.Shelter;
import com.disasterrelief.util.FileLogger;

import java.sql.SQLException;
import java.util.List;

public class ShelterService {

    private final ShelterDAO shelterDAO = new ShelterDAO();

    public List<Shelter> getAllShelters() throws DatabaseException {
        try {
            return shelterDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch shelters: " + e.getMessage(), e);
        }
    }

    public Shelter getShelterById(int id) throws ShelterNotFoundException, DatabaseException {
        try {
            Shelter s = shelterDAO.findById(id);
            if (s == null) {
                throw new ShelterNotFoundException(id);
            }
            return s;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query shelter: " + e.getMessage(), e);
        }
    }

    public Shelter getShelterByUserId(int userId) throws DatabaseException {
        try {
            return shelterDAO.findByUserId(userId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to query shelter for user: " + e.getMessage(), e);
        }
    }

    public int registerShelter(Shelter shelter) throws InvalidRequestException, DatabaseException {
        if (shelter.getName() == null || shelter.getName().trim().isEmpty()) {
            throw new InvalidRequestException("name", "Shelter name cannot be empty.");
        }
        if (shelter.getLocation() == null || shelter.getLocation().trim().isEmpty()) {
            throw new InvalidRequestException("location", "Shelter location cannot be empty.");
        }
        if (shelter.getCapacity() <= 0) {
            throw new InvalidRequestException("capacity", "Shelter capacity must be greater than zero.");
        }

        try {
            int id = shelterDAO.create(shelter);
            FileLogger.logSystem("INFO", "SHELTER", "Registered new shelter: " + shelter.getName() + " (Capacity: " + shelter.getCapacity() + ")");
            return id;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert shelter: " + e.getMessage(), e);
        }
    }

    public boolean updateShelter(Shelter shelter) throws DatabaseException {
        try {
            return shelterDAO.update(shelter);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update shelter: " + e.getMessage(), e);
        }
    }
}
