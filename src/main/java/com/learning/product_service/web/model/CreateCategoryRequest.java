package com.learning.product_service.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        Long parentId,

        @NotBlank(message = "Slug is required")
        @Size(max = 120, message = "Slug must be at most 120 characters")
        @Pattern(regexp = "[a-z0-9]+(-[a-z0-9]+)*", message = "Slug must be lowercase words joined by '-'")
        String slug,

        @NotBlank(message = "Name is required")
        @Size(max = 160, message = "Name must be at most 160 characters")
        String name
) {
}
