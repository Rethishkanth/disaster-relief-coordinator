package com.disasterrelief.exception;

/**
 * Base Application Exception for the Disaster Relief Resource Coordinator system.
 */
public class DisasterReliefException extends Exception {
    private static final long serialVersionUID = 1L;

    public DisasterReliefException(String message) {
        super(message);
    }

    public DisasterReliefException(String message, Throwable cause) {
        super(message, cause);
    }
}
