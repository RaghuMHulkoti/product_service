package com.learning.product_service.gateway.entity;

import com.learning.product_service.core.domain.MediaKind;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "product_media")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMediaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "kind", nullable = false, length = 20)
    private MediaKind kind;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "alt_text")
    private String altText;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
