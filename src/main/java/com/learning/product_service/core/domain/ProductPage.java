package com.learning.product_service.core.domain;

import java.util.List;

public record ProductPage(
        List<Product> items,
        int page,
        int size,
        long totalItems,
        int totalPages
) {
}
