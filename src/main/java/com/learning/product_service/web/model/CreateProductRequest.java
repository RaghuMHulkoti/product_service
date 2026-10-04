package com.learning.product_service.web.model;

import com.learning.product_service.core.domain.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record CreateProductRequest(
        @NotBlank(message = "SKU is required")
        @Size(max = 64, message = "SKU must be at most 64 characters")
        @Pattern(regexp = "[A-Za-z0-9._-]+", message = "SKU may contain only letters, digits, '.', '_' and '-'")
        String sku,

        @Pattern(regexp = "\\d{8,14}", message = "GTIN must be 8 to 14 digits")
        String gtin,

        @NotBlank(message = "Title is required")
        @Size(max = 500, message = "Title must be at most 500 characters")
        String title,

        String description,

        Long brandId,

        @NotNull(message = "Category ID is required")
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
