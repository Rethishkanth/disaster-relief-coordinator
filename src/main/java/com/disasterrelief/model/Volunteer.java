package com.disasterrelief.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Specialized User: Volunteer
 * Represents field workers, drivers, first responders, and medical assistants.
 * Demonstrates:
 * - OOP Inheritance: Subclass of User
 * - Collections: Uses HashSet to store and manage unique skill credentials
 */
public class Volunteer extends User {
    private static final long serialVersionUID = 1L;

    private int volunteerId;
    private String skill; // FIRST_AID, RESCUE, DRIVING, LOGISTICS, MEDICAL
    private int experienceYears;
    private String availabilityStatus; // AVAILABLE, ASSIGNED, OFF_DUTY
    private String location;
    private Set<String> certifications = new HashSet<>();

    public Volunteer() {
        super();
        setRole("VOLUNTEER");
        this.availabilityStatus = "AVAILABLE";
    }

    public Volunteer(int userId, String name, String email, String password, String contact, boolean active,
                     int volunteerId, String skill, int experienceYears, String availabilityStatus, String location) {
        super(userId, name, email, password, "VOLUNTEER", contact, active);
        this.volunteerId = volunteerId;
        this.skill = skill;
        this.experienceYears = experienceYears;
        this.availabilityStatus = availabilityStatus != null ? availabilityStatus : "AVAILABLE";
        this.location = location;
        if (skill != null) {
            this.certifications.add(skill);
        }
    }

    @Override
    public String getRoleTitle() {
        return "Disaster Response Volunteer";
    }

    @Override
    public String getDashboardRoute() {
        return "volunteers";
    }

    @Override
    public boolean canAllocateResources() {
        return false;
    }

    @Override
    public boolean canRegisterSupplies() {
        return false;
    }

    public int getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(int volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
        if (skill != null) {
            this.certifications.add(skill);
        }
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(Set<String> certifications) {
        this.certifications = certifications;
    }

    public void addCertification(String cert) {
        if (cert != null && !cert.trim().isEmpty()) {
            this.certifications.add(cert.trim().toUpperCase());
        }
    }
}
