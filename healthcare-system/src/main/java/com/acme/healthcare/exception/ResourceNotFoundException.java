package com.acme.healthcare.exception;

/**
 * ResourceNotFoundException indicates a requested resource was not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new exception with a message.
     *
     * @param message the error message
     */
    public ResourceNotFoundException(final String message) {
        super(message);
    }

    /**
     * Creates a new exception with a message and cause.
     *
     * @param message the error message
     * @param cause the cause
     */
    public ResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}











