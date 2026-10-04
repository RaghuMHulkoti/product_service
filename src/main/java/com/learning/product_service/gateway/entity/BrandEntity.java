package com.learning.product_service.gateway.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slug", nullable = false, length = 120, unique = true)
    private String slug;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
