package com.learning.product_service.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Attribute schema a category defines for its products, e.g. {@code storage: ENUM [128GB, 256GB]}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAttribute {
    private Long categoryId;
    private String attrKey;
    private AttributeDataType dataType;
    private Boolean required;
    private Boolean facet;
    private List<String> enumValues;
}
