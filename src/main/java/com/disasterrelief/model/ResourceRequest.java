package com.disasterrelief.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Model representing a shelter's emergency resource request.
 * Implements Comparable<ResourceRequest> to enable PriorityQueue ordering
 * based on algorithmic priority score.
 */
public class ResourceRequest implements Comparable<ResourceRequest>, Serializable {
    private static final long serialVersionUID = 1L;

    private int requestId;
    private int shelterId;
    private String shelterName;
    private String shelterLocation;
    private String resourceType;
    private int quantity;
    private String unit;
    private String urgency; // CRITICAL, HIGH, MEDIUM, LOW
    private int peopleAffected;
    private double priorityScore;
    private String status; // PENDING, ALLOCATED, DISPATCHED, DELIVERED, REJECTED
    private String notes;
    private Timestamp requestedDate;
    private String requiredDate;

    public ResourceRequest() {
        this.status = "PENDING";
        this.requestedDate = new Timestamp(System.currentTimeMillis());
    }

    public ResourceRequest(int requestId, int shelterId, String resourceType, int quantity, String unit,
                           String urgency, int peopleAffected, double priorityScore, String status,
                           String notes, Timestamp requestedDate, String requiredDate) {
        this.requestId = requestId;
        this.shelterId = shelterId;
        this.resourceType = resourceType;
        this.quantity = quantity;
        this.unit = unit;
        this.urgency = urgency;
        this.peopleAffected = peopleAffected;
        this.priorityScore = priorityScore;
        this.status = status;
        this.notes = notes;
        this.requestedDate = requestedDate;
        this.requiredDate = requiredDate;
    }

    /**
     * PriorityQueue Ordering:
     * Higher priority score comes first.
     * Ties are broken by earlier requestedDate (FIFO for equal urgency).
     */
    @Override
    public int compareTo(ResourceRequest other) {
        if (other == null) return -1;
        int scoreComparison = Double.compare(other.priorityScore, this.priorityScore);
        if (scoreComparison != 0) {
            return scoreComparison;
        }
        if (this.requestedDate != null && other.requestedDate != null) {
            return this.requestedDate.compareTo(other.requestedDate);
        }
        return Integer.compare(this.requestId, other.requestId);
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
    }

    public String getShelterLocation() {
        return shelterLocation;
    }

    public void setShelterLocation(String shelterLocation) {
        this.shelterLocation = shelterLocation;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public int getPeopleAffected() {
        return peopleAffected;
    }

    public void setPeopleAffected(int peopleAffected) {
        this.peopleAffected = peopleAffected;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    public void setPriorityScore(double priorityScore) {
        this.priorityScore = priorityScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(Timestamp requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }
}
