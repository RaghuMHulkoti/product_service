package com.learning.product_service.web.response;

public record BrandResponse(
        Long id,
        String slug,
        String name,
        Boolean active
) {
}
