package com.learning.product_service.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Catalog product — the canonical description of "what a product is", one row globally.
 *
 * Design rule: no price and no quantity here. Price lives on the supplier offer
 * (Supplier Service) and quantity lives in Inventory Service.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private UUID id;
    private String sku;
    private String gtin;
    private String title;
    private String description;
    private Long brandId;
    private Long categoryId;
    private Map<String, Object> attributes;
    private String mediaRoot;
    private ProductStatus status;
    private Integer weightGrams;
    private Integer dimLengthMm;
    private Integer dimWidthMm;
    private Integer dimHeightMm;
    private Boolean hazardous;
    private Long version;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isPubliclyVisible() {
        return !isDeleted() && status == ProductStatus.ACTIVE;
    }
}
