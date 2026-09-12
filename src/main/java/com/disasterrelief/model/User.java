package com.disasterrelief.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Abstract Base Model for all system actors.
 * Demonstrates:
 * - OOP Encapsulation: Private member fields accessed via controlled getters/setters
 * - OOP Inheritance: Base class extended by Coordinator, Volunteer, Organization, ShelterUser
 * - OOP Polymorphism: Abstract methods implemented uniquely by each subclass
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private int userId;
    private String name;
    private String email;
    private String password;
    private String role; // COORDINATOR, SHELTER, ORGANIZATION, VOLUNTEER
    private String contact;
    private boolean active;
    private Timestamp createdAt;

    public User() {
        this.active = true;
    }

    public User(int userId, String name, String email, String password, String role, String contact, boolean active) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.contact = contact;
        this.active = active;
    }

    // Polymorphic operations
    public abstract String getRoleTitle();
    public abstract String getDashboardRoute();
    public abstract boolean canAllocateResources();
    public abstract boolean canRegisterSupplies();

    // Getters and Setters (Encapsulation)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "[" + role + "] " + name + " (" + email + ")";
    }
}
