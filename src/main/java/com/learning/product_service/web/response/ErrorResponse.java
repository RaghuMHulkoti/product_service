package com.learning.product_service.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * Standard error response format for REST APIs.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private String message;
    private int status;
    private OffsetDateTime timestamp;
    private Map<String, String> details;
}
