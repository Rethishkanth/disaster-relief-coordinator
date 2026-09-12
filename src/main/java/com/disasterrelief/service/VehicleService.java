package com.disasterrelief.service;

import com.disasterrelief.dao.VehicleDAO;
import com.disasterrelief.exception.DatabaseException;
import com.disasterrelief.exception.InvalidRequestException;
import com.disasterrelief.model.Vehicle;
import com.disasterrelief.util.FileLogger;

import java.sql.SQLException;
import java.util.List;

public class VehicleService {

    private final VehicleDAO vehicleDAO = new VehicleDAO();

    public List<Vehicle> getAllVehicles() throws DatabaseException {
        try {
            return vehicleDAO.findAll();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch vehicles: " + e.getMessage(), e);
        }
    }

    public List<Vehicle> getAvailableVehicles() throws DatabaseException {
        try {
            return vehicleDAO.findAvailable();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch available vehicles: " + e.getMessage(), e);
        }
    }

    public Vehicle getVehicleById(int id) throws DatabaseException {
        try {
            return vehicleDAO.findById(id);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch vehicle #" + id + ": " + e.getMessage(), e);
        }
    }

    public int registerVehicle(Vehicle vehicle) throws InvalidRequestException, DatabaseException {
        if (vehicle.getVehicleNumber() == null || vehicle.getVehicleNumber().trim().isEmpty()) {
            throw new InvalidRequestException("vehicleNumber", "Vehicle registration number is required.");
        }
        if (vehicle.getCapacityKg() <= 0) {
            throw new InvalidRequestException("capacityKg", "Vehicle capacity must be greater than zero.");
        }

        try {
            int id = vehicleDAO.create(vehicle);
            FileLogger.logSystem("INFO", "VEHICLE", "Registered vehicle: " + vehicle.getVehicleNumber() + " (" + vehicle.getVehicleType() + ")");
            return id;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to register vehicle: " + e.getMessage(), e);
        }
    }

    public boolean updateStatus(int vehicleId, String status) throws DatabaseException {
        try {
            return vehicleDAO.updateStatus(vehicleId, status);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update vehicle status: " + e.getMessage(), e);
        }
    }
}
