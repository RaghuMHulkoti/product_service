package com.learning.product_service.exception;

import java.util.List;

/**
 * Request is well-formed but breaks a catalog rule, e.g. a required category attribute is missing.
 */
public class BusinessRuleViolationException extends RuntimeException {
    private final List<String> violations;

    public BusinessRuleViolationException(String message, List<String> violations) {
        super(message);
        this.violations = violations;
    }

    public BusinessRuleViolationException(String message) {
        this(message, List.of());
    }

    public List<String> getViolations() {
        return violations;
    }
}
