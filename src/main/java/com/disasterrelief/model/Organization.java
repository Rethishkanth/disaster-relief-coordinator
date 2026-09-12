package com.disasterrelief.model;

/**
 * Specialized User: Organization
 * Represents NGOs, relief agencies, government stockpiles, and corporate aid partners.
 */
public class Organization extends User {
    private static final long serialVersionUID = 1L;

    private int organizationId;
    private String orgName;
    private String orgType; // NGO, GOVT, RED_CROSS, PRIVATE, COMMUNITY
    private String contactPerson;
    private String phone;
    private String address;

    public Organization() {
        super();
        setRole("ORGANIZATION");
    }

    public Organization(int userId, String name, String email, String password, String contact, boolean active,
                        int organizationId, String orgName, String orgType, String contactPerson, String phone, String address) {
        super(userId, name, email, password, "ORGANIZATION", contact, active);
        this.organizationId = organizationId;
        this.orgName = orgName;
        this.orgType = orgType;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.address = address;
    }

    @Override
    public String getRoleTitle() {
        return "Relief Aid Organization";
    }

    @Override
    public String getDashboardRoute() {
        return "resources";
    }

    @Override
    public boolean canAllocateResources() {
        return false;
    }

    @Override
    public boolean canRegisterSupplies() {
        return true;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
