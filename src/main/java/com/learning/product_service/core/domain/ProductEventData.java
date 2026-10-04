package com.learning.product_service.core.domain;

import java.util.Map;
import java.util.UUID;

/**
 * Payload of PRODUCT_CREATED / PRODUCT_UPDATED. Carries enough for the Search indexer to rebuild its document.
 */
public record ProductEventData(
        UUID productId,
        String sku,
        String gtin,
        String title,
        Long brandId,
        Long categoryId,
        String categoryPath,
        Map<String, Object> attributes,
        ProductStatus status,
        Long version
) {
    public static ProductEventData of(Product product, Category category) {
        return new ProductEventData(
                product.getId(),
                product.getSku(),
                product.getGtin(),
                product.getTitle(),
                product.getBrandId(),
                product.getCategoryId(),
                category.getPath(),
                product.getAttributes(),
                product.getStatus(),
                product.getVersion()
        );
    }
}
