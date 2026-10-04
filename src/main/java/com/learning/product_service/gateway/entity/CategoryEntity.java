package com.learning.product_service.gateway.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "slug", nullable = false, length = 120, unique = true)
    private String slug;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    /** Slug chain from the root, e.g. /electronics/mobiles. TDD uses LTREE; VARCHAR keeps it portable. */
    @Column(name = "path", nullable = false, length = 1000)
    private String path;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
