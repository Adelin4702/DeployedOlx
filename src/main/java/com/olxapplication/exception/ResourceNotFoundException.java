package com.olxapplication.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception signifies that a requested resource could not be found within the system.
 * It's mapped to a 404 NOT FOUND HTTP status code for appropriate client-side handling.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    /**
     * Serial version UID for serialization compatibility.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with a detailed message explaining the resource not found situation.
     *
     * @param message The message providing context about the missing resource.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}