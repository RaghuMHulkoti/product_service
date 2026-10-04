package com.learning.product_service.web.response;

import com.learning.product_service.core.domain.ProductStatus;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        String gtin,
        String title,
        String description,
        Long brandId,
        Long categoryId,
        Map<String, Object> attributes,
        String mediaRoot,
        ProductStatus status,
        Integer weightGrams,
        Integer dimLengthMm,
        Integer dimWidthMm,
        Integer dimHeightMm,
        Boolean hazardous,
        Long version,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
