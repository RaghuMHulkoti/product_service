package com.learning.product_service.web.model;

import com.learning.product_service.core.domain.MediaKind;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AddProductMediaRequest(
        @NotNull(message = "Kind is required")
        MediaKind kind,

        @NotBlank(message = "URL is required")
        @Size(max = 1000, message = "URL must be at most 1000 characters")
        @Pattern(regexp = "https?://\\S+", message = "URL must start with http:// or https://")
        String url,

        @PositiveOrZero(message = "Sort order cannot be negative")
        Integer sortOrder,

        @Size(max = 255, message = "Alt text must be at most 255 characters")
        String altText
) {
}
