package com.learning.product_service.web.response;

import com.learning.product_service.core.domain.MediaKind;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MediaResponse(
        Long id,
        UUID productId,
        MediaKind kind,
        String url,
        Integer sortOrder,
        String altText,
        OffsetDateTime createdAt
) {
}
