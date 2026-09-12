package com.disasterrelief.service;

import com.disasterrelief.dao.UserDAO;
import com.disasterrelief.exception.DisasterReliefException;
import com.disasterrelief.model.User;
import com.disasterrelief.util.FileLogger;

import java.sql.SQLException;

public class AuthService {

    private final UserDAO userDAO = new UserDAO();

    public User login(String email, String password) throws DisasterReliefException {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new DisasterReliefException("Email and password are required.");
        }

        try {
            User user = userDAO.authenticate(email, password);
            if (user == null) {
                FileLogger.logSystem("WARN", "AUTH", "Failed login attempt for email: " + email);
                throw new DisasterReliefException("Invalid credentials or account deactivated.");
            }
            FileLogger.logSystem("INFO", "AUTH", "User logged in: " + user.getEmail() + " (" + user.getRole() + ")");
            return user;
        } catch (SQLException e) {
            throw new DisasterReliefException("Database error during authentication: " + e.getMessage(), e);
        }
    }

    public User getUserById(int userId) throws SQLException {
        return userDAO.findById(userId);
    }
}
