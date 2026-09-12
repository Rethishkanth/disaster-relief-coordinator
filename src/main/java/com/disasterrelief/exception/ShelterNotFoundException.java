package com.disasterrelief.exception;

/**
 * Thrown when an operation references a shelter ID that does not exist in the database.
 */
public class ShelterNotFoundException extends DisasterReliefException {
    private static final long serialVersionUID = 1L;

    public ShelterNotFoundException(int shelterId) {
        super("Shelter with ID " + shelterId + " was not found in the disaster response registry.");
    }
}
