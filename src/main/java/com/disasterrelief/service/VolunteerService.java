package com.disasterrelief.service;

import com.disasterrelief.dao.VolunteerDAO;
import com.disasterrelief.exception.DatabaseException;
import com.disasterrelief.exception.InvalidRequestException;
import com.disasterrelief.model.Volunteer;
import com.disasterrelief.util.FileLogger;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service managing Volunteers.
 * Demonstrates:
 * - Collections: HashSet to maintain and query unique volunteer skills
 */
public class VolunteerService {

    private final VolunteerDAO volunteerDAO = new VolunteerDAO();

    public List<Volunteer> getAllVolunteers() throws DatabaseException {
        try {
            return volunteerDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch volunteers: " + e.getMessage(), e);
        }
    }

    public List<Volunteer> getAvailableVolunteers() throws DatabaseException {
        try {
            return volunteerDAO.findAvailable();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch available volunteers: " + e.getMessage(), e);
        }
    }

    public Volunteer getVolunteerById(int id) throws DatabaseException {
        try {
            return volunteerDAO.findById(id);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch volunteer #" + id + ": " + e.getMessage(), e);
        }
    }

    public Volunteer getVolunteerByUserId(int userId) throws DatabaseException {
        try {
            return volunteerDAO.findByUserId(userId);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find volunteer by user: " + e.getMessage(), e);
        }
    }

    /**
     * Demonstrates Collections: HashSet
     * Returns the set of unique skills represented across all registered volunteers.
     */
    public Set<String> getUniqueSkillSet() throws DatabaseException {
        List<Volunteer> all = getAllVolunteers();
        Set<String> uniqueSkills = new HashSet<>();
        for (Volunteer v : all) {
            if (v.getSkill() != null && !v.getSkill().trim().isEmpty()) {
                uniqueSkills.add(v.getSkill().trim().toUpperCase());
            }
        }
        return uniqueSkills;
    }

    public int registerVolunteer(Volunteer v) throws InvalidRequestException, DatabaseException {
        if (v.getName() == null || v.getName().trim().isEmpty()) {
            throw new InvalidRequestException("name", "Volunteer name is required.");
        }
        if (v.getSkill() == null || v.getSkill().trim().isEmpty()) {
            throw new InvalidRequestException("skill", "Volunteer primary skill is required.");
        }

        try {
            int id = volunteerDAO.create(v);
            FileLogger.logSystem("INFO", "VOLUNTEER", "Registered volunteer: " + v.getName() + " (" + v.getSkill() + ")");
            return id;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to register volunteer: " + e.getMessage(), e);
        }
    }

    public boolean updateStatus(int volunteerId, String status) throws DatabaseException {
        try {
            return volunteerDAO.updateStatus(volunteerId, status);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update volunteer status: " + e.getMessage(), e);
        }
    }
}
