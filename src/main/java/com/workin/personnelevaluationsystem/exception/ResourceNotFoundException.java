package com.workin.personnelevaluationsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus makes Spring automatically return the specified HTTP status code
// when this exception is thrown from a controller method.
@ResponseStatus(HttpStatus.NOT_FOUND) // Maps this exception to HTTP 404 Not Found
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}