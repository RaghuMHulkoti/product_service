package com.learning.product_service.web.model;

import com.learning.product_service.core.domain.AttributeDataType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateCategoryAttributeRequest(
        @NotBlank(message = "Attribute key is required")
        @Size(max = 60, message = "Attribute key must be at most 60 characters")
        @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9_]*", message = "Attribute key must start with a letter and use letters, digits or '_'")
        String attrKey,

        @NotNull(message = "Data type is required")
        AttributeDataType dataType,

        Boolean required,

        Boolean facet,

        List<@NotBlank String> enumValues
) {
}
