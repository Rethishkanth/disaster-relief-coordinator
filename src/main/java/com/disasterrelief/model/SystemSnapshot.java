package com.disasterrelief.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Serializable state snapshot representing the entire disaster relief system state at a specific point in time.
 * Demonstrates:
 * - Java Object Serialization (ObjectOutputStream / ObjectInputStream)
 * - State preservation and disaster recovery without database dependency
 */
public class SystemSnapshot implements Serializable {
    private static final long serialVersionUID = 1001L;

    private String snapshotId;
    private Date createdAt;
    private String description;
    private List<Disaster> disasters = new ArrayList<>();
    private List<Shelter> shelters = new ArrayList<>();
    private List<Resource> resources = new ArrayList<>();
    private List<ResourceRequest> requests = new ArrayList<>();
    private List<Allocation> allocations = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Volunteer> volunteers = new ArrayList<>();

    public SystemSnapshot() {
        this.createdAt = new Date();
        this.snapshotId = "SNAP-" + System.currentTimeMillis();
    }

    public SystemSnapshot(String description) {
        this();
        this.description = description;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Disaster> getDisasters() {
        return disasters;
    }

    public void setDisasters(List<Disaster> disasters) {
        this.disasters = disasters;
    }

    public List<Shelter> getShelters() {
        return shelters;
    }

    public void setShelters(List<Shelter> shelters) {
        this.shelters = shelters;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<ResourceRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<ResourceRequest> requests) {
        this.requests = requests;
    }

    public List<Allocation> getAllocations() {
        return allocations;
    }

    public void setAllocations(List<Allocation> allocations) {
        this.allocations = allocations;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Volunteer> getVolunteers() {
        return volunteers;
    }

    public void setVolunteers(List<Volunteer> volunteers) {
        this.volunteers = volunteers;
    }

    @Override
    public String toString() {
        return "SystemSnapshot [" + snapshotId + " @ " + createdAt + "] - Shelters: " + shelters.size()
                + ", Resources: " + resources.size() + ", Requests: " + requests.size()
                + ", Allocations: " + allocations.size();
    }
}
