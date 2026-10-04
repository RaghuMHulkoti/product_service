package com.learning.product_service.web.mapper;

import com.learning.product_service.core.domain.Brand;
import com.learning.product_service.core.domain.Category;
import com.learning.product_service.core.domain.CategoryAttribute;
import com.learning.product_service.core.domain.Offer;
import com.learning.product_service.core.domain.Product;
import com.learning.product_service.core.domain.ProductMedia;
import com.learning.product_service.core.domain.ProductPage;
import com.learning.product_service.core.processor.product.GetProductProcessorResponse;
import com.learning.product_service.web.response.BrandResponse;
import com.learning.product_service.web.response.CategoryAttributeResponse;
import com.learning.product_service.web.response.CategoryResponse;
import com.learning.product_service.web.response.MediaResponse;
import com.learning.product_service.web.response.OfferResponse;
import com.learning.product_service.web.response.PageResponse;
import com.learning.product_service.web.response.ProductDetailResponse;
import com.learning.product_service.web.response.ProductResponse;

import java.util.Comparator;
import java.util.List;

/**
 * Maps core domain objects to REST response DTOs.
 */
public final class ProductResponseMapper {

    private ProductResponseMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getGtin(),
                product.getTitle(),
                product.getDescription(),
                product.getBrandId(),
                product.getCategoryId(),
                product.getAttributes(),
                product.getMediaRoot(),
                product.getStatus(),
                product.getWeightGrams(),
                product.getDimLengthMm(),
                product.getDimWidthMm(),
                product.getDimHeightMm(),
                product.getHazardous(),
                product.getVersion(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }

    public static PageResponse<ProductResponse> toResponse(ProductPage page) {
        return new PageResponse<>(
                page.items().stream().map(ProductResponseMapper::toResponse).toList(),
                page.page(),
                page.size(),
                page.totalItems(),
                page.totalPages()
        );
    }

    public static ProductDetailResponse toDetailResponse(GetProductProcessorResponse response) {
        var lookup = response.getOfferLookup();
        var offers = lookup == null ? null : lookup.offers().stream()
                .filter(offer -> !Boolean.FALSE.equals(offer.active()))
                .sorted(Comparator.comparing(Offer::price, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        var lowest = offers == null || offers.isEmpty() ? null : offers.getFirst();

        return new ProductDetailResponse(
                toResponse(response.getProduct()),
                response.getBrand() == null ? null : toResponse(response.getBrand()),
                toResponse(response.getCategory()),
                response.getMedia().stream().map(ProductResponseMapper::toResponse).toList(),
                offers == null ? null : offers.stream().map(ProductResponseMapper::toResponse).toList(),
                lookup == null ? null : lookup.available(),
                lowest == null ? null : lowest.price(),
                lowest == null ? null : lowest.currency(),
                offers == null ? null : offers.size()
        );
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getParentId(),
                category.getSlug(),
                category.getName(),
                category.getPath(),
                category.getActive(),
                category.getChildren().stream().map(ProductResponseMapper::toResponse).toList()
        );
    }

    public static List<CategoryResponse> toResponse(List<Category> roots) {
        return roots.stream().map(ProductResponseMapper::toResponse).toList();
    }

    public static CategoryAttributeResponse toResponse(CategoryAttribute attribute) {
        return new CategoryAttributeResponse(
                attribute.getCategoryId(),
                attribute.getAttrKey(),
                attribute.getDataType(),
                attribute.getRequired(),
                attribute.getFacet(),
                attribute.getEnumValues()
        );
    }

    public static BrandResponse toResponse(Brand brand) {
        return new BrandResponse(brand.getId(), brand.getSlug(), brand.getName(), brand.getActive());
    }

    public static MediaResponse toResponse(ProductMedia media) {
        return new MediaResponse(
                media.getId(),
                media.getProductId(),
                media.getKind(),
                media.getUrl(),
                media.getSortOrder(),
                media.getAltText(),
                media.getCreatedAt()
        );
    }

    public static OfferResponse toResponse(Offer offer) {
        return new OfferResponse(
                offer.offerId(),
                offer.supplierId(),
                offer.supplierSku(),
                offer.price(),
                offer.currency(),
                offer.condition(),
                offer.handlingDays(),
                offer.listedAt()
        );
    }
}
