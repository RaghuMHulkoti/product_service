package com.learning.product_service.gateway.entity;

import com.learning.product_service.core.domain.ProductStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

/**
 * Catalog product row. Deliberately has no price and no quantity column.
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    private UUID id;

    @Column(name = "sku", nullable = false, length = 64)
    private String sku;

    @Column(name = "gtin", length = 20)
    private String gtin;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", nullable = false)
    private Map<String, Object> attributes;

    @Column(name = "media_root", length = 500)
    private String mediaRoot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "weight_grams")
    private Integer weightGrams;

    @Column(name = "dim_l_mm")
    private Integer dimLengthMm;

    @Column(name = "dim_w_mm")
    private Integer dimWidthMm;

    @Column(name = "dim_h_mm")
    private Integer dimHeightMm;

    @Column(name = "is_hazardous", nullable = false)
    private Boolean isHazardous;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
