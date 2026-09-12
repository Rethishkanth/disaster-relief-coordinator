package com.disasterrelief.exception;

/**
 * Thrown when a medical supply, vaccine, or food ration batch is expired and cannot be allocated.
 */
public class ResourceExpiredException extends DisasterReliefException {
    private static final long serialVersionUID = 1L;

    public ResourceExpiredException(String resourceName, String expiryDate) {
        super(String.format("Resource '%s' cannot be allocated because it expired on %s",
                resourceName, expiryDate));
    }
}
