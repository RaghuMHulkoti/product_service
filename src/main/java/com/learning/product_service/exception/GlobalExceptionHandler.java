package com.learning.product_service.exception;

import com.learning.product_service.common.StatusCode;
import com.learning.product_service.web.response.ErrorResponse;
import com.learning.product_service.web.response.Response;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Converts exceptions to the standard error envelope and status codes (TDD §11.1).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<ErrorResponse>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.put(
                error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName(),
                error.getDefaultMessage()));

        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request validation failed", errors);
    }

    /** Constraint annotations on @RequestParam / @PathVariable (e.g. size > 100). */
    @ExceptionHandler({HandlerMethodValidationException.class, ConstraintViolationException.class})
    public ResponseEntity<Response<ErrorResponse>> handleParameterValidation(Exception ex) {
        log.warn("Parameter validation error: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Request parameter validation failed", null);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<Response<ErrorResponse>> handleBadRequest(Exception ex) {
        log.warn("Malformed request: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Malformed request: check body, parameters and headers", null);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Response<ErrorResponse>> handleInvalidRequest(InvalidRequestException ex) {
        log.warn("Invalid request: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", ex.getMessage(), null);
    }

    @ExceptionHandler({ProductNotFoundException.class, CategoryNotFoundException.class, BrandNotFoundException.class})
    public ResponseEntity<Response<ErrorResponse>> handleNotFound(RuntimeException ex) {
        log.warn("Not found: {}", ex.getMessage());
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), null);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Response<ErrorResponse>> handleDuplicate(DuplicateResourceException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), null);
    }

    /** Unique index hit by a concurrent insert that passed the existence check. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response<ErrorResponse>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return build(HttpStatus.CONFLICT, "CONFLICT", "The request conflicts with existing data", null);
    }

    @ExceptionHandler({OptimisticLockingException.class, ObjectOptimisticLockingFailureException.class})
    public ResponseEntity<Response<ErrorResponse>> handleOptimisticLocking(RuntimeException ex) {
        log.warn("Optimistic locking failure: {}", ex.getMessage());
        return build(HttpStatus.CONFLICT, "CONFLICT",
                "The resource has been modified by another request. Reload and retry.", null);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<Response<ErrorResponse>> handleBusinessRule(BusinessRuleViolationException ex) {
        log.warn("Business rule violation: {} {}", ex.getMessage(), ex.getViolations());

        Map<String, String> details = new LinkedHashMap<>();
        for (int i = 0; i < ex.getViolations().size(); i++) {
            details.put("violation_" + (i + 1), ex.getViolations().get(i));
        }
        return build(HttpStatus.UNPROCESSABLE_CONTENT, "BUSINESS_RULE_VIOLATION", ex.getMessage(),
                details.isEmpty() ? null : details);
    }

    /**
     * Spring MVC exceptions that already carry an HTTP status (404 no route, 405 wrong method, ...)
     * keep that status; anything else is a 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<ErrorResponse>> handleUnexpectedException(Exception ex) {
        if (ex instanceof org.springframework.web.ErrorResponse springError) {
            HttpStatusCode status = springError.getStatusCode();
            log.warn("Request rejected with {}: {}", status, ex.getMessage());
            return build(status, "REQUEST_REJECTED", ex.getMessage(), null);
        }

        log.error("Unexpected error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL",
                "An unexpected error occurred. Please contact support.", null);
    }

    private ResponseEntity<Response<ErrorResponse>> build(HttpStatusCode status, String errorCode,
                                                         String message, Map<String, String> details) {
        var errorResponse = ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .status(status.value())
                .timestamp(OffsetDateTime.now(ZoneOffset.UTC))
                .details(details)
                .build();

        return ResponseEntity.status(status).body(new Response<>(StatusCode.FAILURE, message, errorResponse));
    }
}
