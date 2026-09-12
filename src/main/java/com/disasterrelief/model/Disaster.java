package com.disasterrelief.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Disaster implements Serializable {
    private static final long serialVersionUID = 1L;

    private int disasterId;
    private String name;
    private String disasterType; // FLOOD, EARTHQUAKE, CYCLONE, LANDSLIDE
    private String location;
    private String severity; // CRITICAL, HIGH, MEDIUM, LOW
    private String status; // ACTIVE, CONTAINED, RESOLVED
    private String description;
    private Timestamp createdAt;

    public Disaster() {
        this.status = "ACTIVE";
    }

    public Disaster(int disasterId, String name, String disasterType, String location, String severity, String status, String description) {
        this.disasterId = disasterId;
        this.name = name;
        this.disasterType = disasterType;
        this.location = location;
        this.severity = severity;
        this.status = status;
        this.description = description;
    }

    public int getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(int disasterId) {
        this.disasterId = disasterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisasterType() {
        return disasterType;
    }

    public void setDisasterType(String disasterType) {
        this.disasterType = disasterType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getSeverityBonus() {
        if ("CRITICAL".equalsIgnoreCase(severity)) return 25;
        if ("HIGH".equalsIgnoreCase(severity)) return 15;
        if ("MEDIUM".equalsIgnoreCase(severity)) return 5;
        return 0;
    }
}
