package com.disasterrelief.exception;

/**
 * Wraps low-level SQLExceptions into domain-meaningful database exceptions.
 */
public class DatabaseException extends DisasterReliefException {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
