package com.learning.product_service.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductMedia {
    private Long id;
    private UUID productId;
    private MediaKind kind;
    private String url;
    private Integer sortOrder;
    private String altText;
    private OffsetDateTime createdAt;
}
