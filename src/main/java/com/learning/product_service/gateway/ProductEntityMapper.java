package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.Brand;
import com.learning.product_service.core.domain.Category;
import com.learning.product_service.core.domain.CategoryAttribute;
import com.learning.product_service.core.domain.Product;
import com.learning.product_service.core.domain.ProductMedia;
import com.learning.product_service.gateway.entity.BrandEntity;
import com.learning.product_service.gateway.entity.CategoryAttributeEntity;
import com.learning.product_service.gateway.entity.CategoryEntity;
import com.learning.product_service.gateway.entity.ProductEntity;
import com.learning.product_service.gateway.entity.ProductMediaEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Persistence-layer mapper — the ONLY place where domain objects and JPA entities cross each other.
 */
public class ProductEntityMapper {

    // =========================================================================
    // Product
    // =========================================================================

    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        return Product.builder()
                .id(entity.getId())
                .sku(entity.getSku())
                .gtin(entity.getGtin())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .brandId(entity.getBrandId())
                .categoryId(entity.getCategoryId())
                .attributes(entity.getAttributes() != null ? new HashMap<>(entity.getAttributes()) : new HashMap<>())
                .mediaRoot(entity.getMediaRoot())
                .status(entity.getStatus())
                .weightGrams(entity.getWeightGrams())
                .dimLengthMm(entity.getDimLengthMm())
                .dimWidthMm(entity.getDimWidthMm())
                .dimHeightMm(entity.getDimHeightMm())
                .hazardous(entity.getIsHazardous())
                .version(entity.getVersion())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .deletedAt(entity.getDeletedAt())
                .build();
    }

    public ProductEntity toEntity(Product domain) {
        if (domain == null) return null;
        return ProductEntity.builder()
                .id(domain.getId())
                .sku(domain.getSku())
                .gtin(domain.getGtin())
                .title(domain.getTitle())
                .description(domain.getDescription())
                .brandId(domain.getBrandId())
                .categoryId(domain.getCategoryId())
                .attributes(domain.getAttributes() != null ? new HashMap<>(domain.getAttributes()) : new HashMap<>())
                .mediaRoot(domain.getMediaRoot())
                .status(domain.getStatus())
                .weightGrams(domain.getWeightGrams())
                .dimLengthMm(domain.getDimLengthMm())
                .dimWidthMm(domain.getDimWidthMm())
                .dimHeightMm(domain.getDimHeightMm())
                .isHazardous(domain.getHazardous() != null ? domain.getHazardous() : Boolean.FALSE)
                .version(domain.getVersion())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .deletedAt(domain.getDeletedAt())
                .build();
    }

    // =========================================================================
    // Category
    // =========================================================================

    public Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return Category.builder()
                .id(entity.getId())
                .parentId(entity.getParentId())
                .slug(entity.getSlug())
                .name(entity.getName())
                .path(entity.getPath())
                .active(entity.getIsActive())
                .build();
    }

    public CategoryEntity toEntity(Category domain) {
        if (domain == null) return null;
        return CategoryEntity.builder()
                .id(domain.getId())
                .parentId(domain.getParentId())
                .slug(domain.getSlug())
                .name(domain.getName())
                .path(domain.getPath())
                .isActive(domain.getActive() != null ? domain.getActive() : Boolean.TRUE)
                .build();
    }

    // =========================================================================
    // CategoryAttribute
    // =========================================================================

    public CategoryAttribute toDomain(CategoryAttributeEntity entity) {
        if (entity == null) return null;
        return CategoryAttribute.builder()
                .categoryId(entity.getCategoryId())
                .attrKey(entity.getAttrKey())
                .dataType(entity.getDataType())
                .required(entity.getIsRequired())
                .facet(entity.getIsFacet())
                .enumValues(entity.getEnumValues() != null ? List.copyOf(entity.getEnumValues()) : null)
                .build();
    }

    public CategoryAttributeEntity toEntity(CategoryAttribute domain) {
        if (domain == null) return null;
        return CategoryAttributeEntity.builder()
                .categoryId(domain.getCategoryId())
                .attrKey(domain.getAttrKey())
                .dataType(domain.getDataType())
                .isRequired(Boolean.TRUE.equals(domain.getRequired()))
                .isFacet(Boolean.TRUE.equals(domain.getFacet()))
                .enumValues(domain.getEnumValues())
                .build();
    }

    // =========================================================================
    // Brand
    // =========================================================================

    public Brand toDomain(BrandEntity entity) {
        if (entity == null) return null;
        return Brand.builder()
                .id(entity.getId())
                .slug(entity.getSlug())
                .name(entity.getName())
                .active(entity.getIsActive())
                .build();
    }

    public BrandEntity toEntity(Brand domain) {
        if (domain == null) return null;
        return BrandEntity.builder()
                .id(domain.getId())
                .slug(domain.getSlug())
                .name(domain.getName())
                .isActive(domain.getActive() != null ? domain.getActive() : Boolean.TRUE)
                .build();
    }

    // =========================================================================
    // ProductMedia
    // =========================================================================

    public ProductMedia toDomain(ProductMediaEntity entity) {
        if (entity == null) return null;
        return ProductMedia.builder()
                .id(entity.getId())
                .productId(entity.getProductId())
                .kind(entity.getKind())
                .url(entity.getUrl())
                .sortOrder(entity.getSortOrder())
                .altText(entity.getAltText())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ProductMediaEntity toEntity(ProductMedia domain) {
        if (domain == null) return null;
        return ProductMediaEntity.builder()
                .id(domain.getId())
                .productId(domain.getProductId())
                .kind(domain.getKind())
                .url(domain.getUrl())
                .sortOrder(domain.getSortOrder() != null ? domain.getSortOrder() : 0)
                .altText(domain.getAltText())
                .build();
    }
}
