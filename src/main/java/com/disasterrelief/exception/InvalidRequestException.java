package com.disasterrelief.exception;

/**
 * Thrown when submitted resource request data is invalid, incomplete, or violates business constraints.
 */
public class InvalidRequestException extends DisasterReliefException {
    private static final long serialVersionUID = 1L;

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String field, String reason) {
        super("Invalid request on field '" + field + "': " + reason);
    }
}
