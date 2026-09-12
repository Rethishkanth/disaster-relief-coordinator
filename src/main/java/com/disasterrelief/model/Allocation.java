package com.disasterrelief.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Allocation implements Serializable {
    private static final long serialVersionUID = 1L;

    private int allocationId;
    private int requestId;
    private int resourceId;
    private String resourceName;
    private String resourceType;
    private int allocatedQuantity;
    private String unit;
    private Integer volunteerId;
    private String volunteerName;
    private Integer vehicleId;
    private String vehicleNumber;
    private String vehicleType;
    private String shelterName;
    private String shelterLocation;
    private Timestamp allocationDate;
    private Timestamp dispatchDate;
    private Timestamp deliveryDate;
    private String status; // PREPARING, DISPATCHED, IN_TRANSIT, DELIVERED, CANCELLED
    private String trackingNotes;

    public Allocation() {
        this.status = "PREPARING";
        this.allocationDate = new Timestamp(System.currentTimeMillis());
    }

    public Allocation(int allocationId, int requestId, int resourceId, int allocatedQuantity,
                      Integer volunteerId, Integer vehicleId, Timestamp allocationDate,
                      Timestamp dispatchDate, Timestamp deliveryDate, String status, String trackingNotes) {
        this.allocationId = allocationId;
        this.requestId = requestId;
        this.resourceId = resourceId;
        this.allocatedQuantity = allocatedQuantity;
        this.volunteerId = volunteerId;
        this.vehicleId = vehicleId;
        this.allocationDate = allocationDate;
        this.dispatchDate = dispatchDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
        this.trackingNotes = trackingNotes;
    }

    public int getAllocationId() {
        return allocationId;
    }

    public void setAllocationId(int allocationId) {
        this.allocationId = allocationId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
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

    public int getAllocatedQuantity() {
        return allocatedQuantity;
    }

    public void setAllocatedQuantity(int allocatedQuantity) {
        this.allocatedQuantity = allocatedQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(Integer volunteerId) {
        this.volunteerId = volunteerId;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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

    public Timestamp getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Timestamp allocationDate) {
        this.allocationDate = allocationDate;
    }

    public Timestamp getDispatchDate() {
        return dispatchDate;
    }

    public void setDispatchDate(Timestamp dispatchDate) {
        this.dispatchDate = dispatchDate;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrackingNotes() {
        return trackingNotes;
    }

    public void setTrackingNotes(String trackingNotes) {
        this.trackingNotes = trackingNotes;
    }
}
