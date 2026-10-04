package com.learning.product_service.web.response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Product detail: catalog data plus live offers from Supplier Service.
 * {@code offers} is null when offers were not requested (internal endpoint).
 * {@code offersAvailable = false} means Supplier Service did not answer, so {@code offers} is empty.
 */
public record ProductDetailResponse(
        ProductResponse product,
        BrandResponse brand,
        CategoryResponse category,
        List<MediaResponse> media,
        List<OfferResponse> offers,
        Boolean offersAvailable,
        BigDecimal lowestOfferPrice,
        String lowestOfferCurrency,
        Integer availableFromCount
) {
}
