package com.disasterrelief.model;

/**
 * Stages in the delivery/dispatch lifecycle
 */
public enum DeliveryStatus {
    PREPARING("Preparing at Depot", 20),
    DISPATCHED("Dispatched / En Route", 50),
    IN_TRANSIT("In Transit to Sector", 75),
    DELIVERED("Delivered & Verified", 100),
    CANCELLED("Cancelled", 0);

    private final String label;
    private final int progressPercentage;

    DeliveryStatus(String label, int progressPercentage) {
        this.label = label;
        this.progressPercentage = progressPercentage;
    }

    public String getLabel() {
        return label;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public static DeliveryStatus fromString(String text) {
        if (text == null) return PREPARING;
        for (DeliveryStatus s : DeliveryStatus.values()) {
            if (s.name().equalsIgnoreCase(text.trim()) || s.getLabel().equalsIgnoreCase(text.trim())) {
                return s;
            }
        }
        return PREPARING;
    }
}
