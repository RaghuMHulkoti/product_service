package com.learning.product_service.gateway.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Product outbox row (Transactional Outbox). A poller or CDC (future work) publishes
 * PENDING rows to the domain.product.events topic and marks them SENT.
 */
@Entity
@Table(name = "product_outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false, length = 60)
    private String eventType;

    /** JSON event envelope (eventId, eventType, occurredAt, producer, version, data). */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false)
    private String payload;

    /** PENDING → SENT | FAILED */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        if (status == null) status = "PENDING";
    }
}
