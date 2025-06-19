package com.workin.personnelevaluationsystem.exception;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPropertyException.class)
    public ResponseEntity<Object> handleInvalidPropertyException(InvalidPropertyException ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", "There was an error binding form data. Please check the submitted fields. Details: " + ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> {
                            if (error instanceof FieldError) {
                                return ((FieldError) error).getField();
                            }
                            return error.getObjectName();
                        },
                        error -> error.getDefaultMessage()
                ));

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", "Input validation failed");
        body.put("validationErrors", errors);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all database-related exceptions to prevent exposing SQL details to users
     */
    @ExceptionHandler({
        SQLException.class,
        DataAccessException.class,
        DataIntegrityViolationException.class,
        JDBCConnectionException.class,
        PersistenceException.class,
        ConstraintViolationException.class,
        SQLGrammarException.class,
        BadSqlGrammarException.class
    })
    public ResponseEntity<Object> handleSQLException(Exception ex, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());

        String userMessage;
        HttpStatus status;

        // Provide more specific messages for constraint violations (but still no SQL details)
        if (ex instanceof DataIntegrityViolationException || ex instanceof ConstraintViolationException
                || (ex.getCause() != null && (ex.getCause() instanceof DataIntegrityViolationException
                || ex.getCause() instanceof ConstraintViolationException))) {
            userMessage = "Can't complete the operation as the entity is in active use.";
            status = HttpStatus.CONFLICT;
            body.put("error", "Data Constraint Violation");
        } else {
            userMessage = "A database error has occurred. Please contact your system administrator.";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body.put("error", "Database Error");
        }

        body.put("status", status.value());
        body.put("message", userMessage);
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Log the actual exception for administrators/developers
        System.err.println("SQL/Database exception encountered:");
        ex.printStackTrace();

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        // Check if this is a SQL-related exception that might have been missed by the specific handlers
        if (ex.getMessage() != null &&
            (ex.getMessage().contains("SQL") ||
             ex.getMessage().contains("database") ||
             ex.getMessage().contains("constraint") ||
             ex.getMessage().contains("FK__") ||
             ex.getMessage().contains("DELETE statement conflicted"))) {
            return handleSQLException(ex, request);
        }

        // Also check the cause chain for SQL-related exceptions
        Throwable cause = ex.getCause();
        while (cause != null) {
            if (cause instanceof SQLException ||
                cause instanceof DataAccessException ||
                cause instanceof PersistenceException ||
                (cause.getMessage() != null &&
                (cause.getMessage().contains("SQL") ||
                 cause.getMessage().contains("database") ||
                 cause.getMessage().contains("constraint")))) {
                return handleSQLException(ex, request);
            }
            cause = cause.getCause();
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred. Please try again later or contact support if the issue persists.");
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Log the actual exception for administrators/developers
        ex.printStackTrace();

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}