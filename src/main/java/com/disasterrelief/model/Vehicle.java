package com.disasterrelief.model;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private int vehicleId;
    private String vehicleNumber;
    private String vehicleType; // TRUCK, VAN, BOAT, HELICOPTER, AMBULANCE
    private int capacityKg;
    private String driverName;
    private String contact;
    private String availabilityStatus; // AVAILABLE, IN_TRANSIT, MAINTENANCE

    public Vehicle() {
        this.availabilityStatus = "AVAILABLE";
    }

    public Vehicle(int vehicleId, String vehicleNumber, String vehicleType, int capacityKg,
                   String driverName, String contact, String availabilityStatus) {
        this.vehicleId = vehicleId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.capacityKg = capacityKg;
        this.driverName = driverName;
        this.contact = contact;
        this.availabilityStatus = availabilityStatus;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
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

    public int getCapacityKg() {
        return capacityKg;
    }

    public void setCapacityKg(int capacityKg) {
        this.capacityKg = capacityKg;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }
}
