package com.learning.product_service.web.response;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DeleteProductResponse(
        UUID productId,
        OffsetDateTime deletedAt
) {
}
