package com.disasterrelief.exception;

/**
 * Thrown when no suitable transport vehicle or qualified volunteer driver is available for dispatch.
 */
public class DeliveryUnavailableException extends DisasterReliefException {
    private static final long serialVersionUID = 1L;

    public DeliveryUnavailableException(String reason) {
        super("Delivery dispatch unavailable: " + reason);
    }
}
