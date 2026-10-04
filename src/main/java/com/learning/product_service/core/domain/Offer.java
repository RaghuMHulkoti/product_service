package com.learning.product_service.core.domain;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Read-only view of a supplier offer, fetched from Supplier Service. Product Service never stores it.
 */
public record Offer(
        UUID offerId,
        UUID supplierId,
        UUID productId,
        String supplierSku,
        BigDecimal price,
        String currency,
        String condition,
        Short handlingDays,
        Boolean active,
        OffsetDateTime listedAt
) {
}
