package com.disasterrelief.exception;

/**
 * Thrown when requested supply quantity exceeds available inventory stock in warehouses.
 */
public class InsufficientResourceException extends DisasterReliefException {
    private static final long serialVersionUID = 1L;

    private final String resourceType;
    private final int requestedQuantity;
    private final int availableQuantity;

    public InsufficientResourceException(String resourceType, int requestedQuantity, int availableQuantity) {
        super(String.format("Insufficient resources for '%s'. Requested: %d, Available in stock: %d",
                resourceType, requestedQuantity, availableQuantity));
        this.resourceType = resourceType;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }

    public String getResourceType() {
        return resourceType;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
