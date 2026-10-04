package com.learning.product_service.web.response;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record OfferResponse(
        UUID offerId,
        UUID supplierId,
        String supplierSku,
        BigDecimal price,
        String currency,
        String condition,
        Short handlingDays,
        OffsetDateTime listedAt
) {
}
