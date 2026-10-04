package com.learning.product_service.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Filters for product listing. Null fields are ignored. Soft-deleted products are always excluded.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilter {
    private List<UUID> ids;
    private List<Long> categoryIds;
    private Long brandId;
    private ProductStatus status;
    private String query;
}
