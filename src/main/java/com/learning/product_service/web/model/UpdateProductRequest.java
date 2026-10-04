package com.learning.product_service.web.model;

import com.learning.product_service.core.domain.ProductStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * PATCH body. Every field is optional; a missing field is left unchanged.
 * {@code version} enables optimistic locking: send the version you read, get 409 if it moved on.
 * {@code attributes} replaces the whole attribute map.
 */
public record UpdateProductRequest(
        Long version,

        @Pattern(regexp = "\\d{8,14}", message = "GTIN must be 8 to 14 digits")
        String gtin,

        @Size(min = 1, max = 500, message = "Title must be 1 to 500 characters")
        String title,

        String description,

        Long brandId,

        Long categoryId,

        Map<String, Object> attributes,

        @Size(max = 500, message = "Media root must be at most 500 characters")
        String mediaRoot,

        ProductStatus status,

        @PositiveOrZero(message = "Weight cannot be negative")
        Integer weightGrams,

        @PositiveOrZero(message = "Length cannot be negative")
        Integer dimLengthMm,

        @PositiveOrZero(message = "Width cannot be negative")
        Integer dimWidthMm,

        @PositiveOrZero(message = "Height cannot be negative")
        Integer dimHeightMm,

        Boolean hazardous
) {
}
