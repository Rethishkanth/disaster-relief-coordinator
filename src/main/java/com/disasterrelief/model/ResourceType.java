package com.disasterrelief.model;

/**
 * Standard classification of relief supplies
 */
public enum ResourceType {
    WATER("Clean Drinking Water", "Bottles"),
    FOOD("Ready-to-Eat Food / Rations", "Packs"),
    MEDICINE("Emergency Medical Supplies", "Kits"),
    SHELTER_KIT("Temporary Tents / Tarpaulins", "Sets"),
    CLOTHING("Blankets & Warm Clothing", "Pieces"),
    RESCUE_GEAR("Rafts, Ropes, Flashlights", "Units"),
    OTHER("General Relief Supplies", "Units");

    private final String displayName;
    private final String defaultUnit;

    ResourceType(String displayName, String defaultUnit) {
        this.displayName = displayName;
        this.defaultUnit = defaultUnit;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public static ResourceType fromString(String text) {
        if (text == null) return OTHER;
        for (ResourceType t : ResourceType.values()) {
            if (t.name().equalsIgnoreCase(text.trim()) || t.getDisplayName().equalsIgnoreCase(text.trim())) {
                return t;
            }
        }
        return OTHER;
    }
}
