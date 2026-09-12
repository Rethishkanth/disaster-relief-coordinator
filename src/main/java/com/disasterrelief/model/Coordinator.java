package com.disasterrelief.model;

/**
 * Specialized User: Coordinator
 * Represents incident commanders and disaster coordinators who have full authority
 * to review requests, trigger manual or automated allocation, dispatch vehicles, and track operations.
 */
public class Coordinator extends User {
    private static final long serialVersionUID = 1L;
    
    private String jurisdiction;
    private String badgeNumber;

    public Coordinator() {
        super();
        setRole("COORDINATOR");
    }

    public Coordinator(int userId, String name, String email, String password, String contact, boolean active) {
        super(userId, name, email, password, "COORDINATOR", contact, active);
    }

    @Override
    public String getRoleTitle() {
        return "Incident Relief Coordinator";
    }

    @Override
    public String getDashboardRoute() {
        return "dashboard";
    }

    @Override
    public boolean canAllocateResources() {
        return true;
    }

    @Override
    public boolean canRegisterSupplies() {
        return true;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }
}
