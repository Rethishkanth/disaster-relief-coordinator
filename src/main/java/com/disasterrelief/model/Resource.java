package com.disasterrelief.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Resource implements Serializable {
    private static final long serialVersionUID = 1L;

    private int resourceId;
    private Integer organizationId;
    private String orgName;
    private String resourceName;
    private String resourceType; // FOOD, WATER, MEDICINE, SHELTER_KIT, CLOTHING, RESCUE_GEAR
    private int quantity;
    private String unit;
    private String expiryDate;
    private String availabilityStatus; // AVAILABLE, LOW_STOCK, DEPLETED, EXPIRED
    private String description;
    private Timestamp createdAt;

    public Resource() {
        this.availabilityStatus = "AVAILABLE";
    }

    public Resource(int resourceId, Integer organizationId, String resourceName, String resourceType,
                    int quantity, String unit, String expiryDate, String availabilityStatus, String description) {
        this.resourceId = resourceId;
        this.organizationId = organizationId;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.availabilityStatus = availabilityStatus;
        this.description = description;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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
        if (this.quantity <= 0) {
            this.availabilityStatus = "DEPLETED";
        } else if (this.quantity < 50) {
            this.availabilityStatus = "LOW_STOCK";
        } else {
            this.availabilityStatus = "AVAILABLE";
        }
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
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
}
