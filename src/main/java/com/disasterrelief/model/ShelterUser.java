package com.disasterrelief.model;

/**
 * Specialized User: ShelterUser
 * Represents managers stationed at relief camps, community centers, and evacuation hubs.
 */
public class ShelterUser extends User {
    private static final long serialVersionUID = 1L;

    private int shelterId;
    private String shelterName;

    public ShelterUser() {
        super();
        setRole("SHELTER");
    }

    public ShelterUser(int userId, String name, String email, String password, String contact, boolean active,
                       int shelterId, String shelterName) {
        super(userId, name, email, password, "SHELTER", contact, active);
        this.shelterId = shelterId;
        this.shelterName = shelterName;
    }

    @Override
    public String getRoleTitle() {
        return "Relief Shelter Manager";
    }

    @Override
    public String getDashboardRoute() {
        return "shelters";
    }

    @Override
    public boolean canAllocateResources() {
        return false;
    }

    @Override
    public boolean canRegisterSupplies() {
        return false;
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
}
