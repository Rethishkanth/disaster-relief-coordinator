package com.disasterrelief.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Shelter implements Serializable {
    private static final long serialVersionUID = 1L;

    private int shelterId;
    private Integer disasterId;
    private String disasterName;
    private Integer userId;
    private String managerName;
    private String name;
    private String location;
    private int capacity;
    private int currentPopulation;
    private String contact;
    private String status; // OPERATIONAL, AT_CAPACITY, EVACUATING, CLOSED
    private Timestamp createdAt;

    public Shelter() {
        this.status = "OPERATIONAL";
    }

    public Shelter(int shelterId, Integer disasterId, Integer userId, String name, String location,
                   int capacity, int currentPopulation, String contact, String status) {
        this.shelterId = shelterId;
        this.disasterId = disasterId;
        this.userId = userId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.currentPopulation = currentPopulation;
        this.contact = contact;
        this.status = status;
    }

    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
    }

    public Integer getDisasterId() {
        return disasterId;
    }

    public void setDisasterId(Integer disasterId) {
        this.disasterId = disasterId;
    }

    public String getDisasterName() {
        return disasterName;
    }

    public void setDisasterName(String disasterName) {
        this.disasterName = disasterName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentPopulation() {
        return currentPopulation;
    }

    public void setCurrentPopulation(int currentPopulation) {
        this.currentPopulation = currentPopulation;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public double getOccupancyRate() {
        if (capacity <= 0) return 0.0;
        return ((double) currentPopulation / capacity) * 100.0;
    }
}
