package com.learning.product_service.core.service;

import com.learning.product_service.common.EventType;
import com.learning.product_service.gateway.ProductOutboxGateway;
import com.learning.product_service.metrics.BusinessMetrics;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Writes domain events to the product outbox using the standard event envelope (TDD §13.3).
 */
public class OutboxService {

    private static final String PRODUCER = "product-service";
    private static final int ENVELOPE_VERSION = 1;

    private final ProductOutboxGateway productOutboxGateway;
    private final JsonMapper jsonMapper;
    private final BusinessMetrics businessMetrics;

    public OutboxService(ProductOutboxGateway productOutboxGateway, JsonMapper jsonMapper,
                         BusinessMetrics businessMetrics) {
        this.productOutboxGateway = productOutboxGateway;
        this.jsonMapper = jsonMapper;
        this.businessMetrics = businessMetrics;
    }

    public void append(UUID aggregateId, EventType eventType, Object data) {
        var envelope = new EventEnvelope(
                UUID.randomUUID(),
                eventType.name(),
                OffsetDateTime.now(ZoneOffset.UTC),
                PRODUCER,
                ENVELOPE_VERSION,
                data
        );
        try {
            productOutboxGateway.append(aggregateId, eventType.name(), jsonMapper.writeValueAsString(envelope));
            businessMetrics.outboxEventAppended(eventType.name());
        } catch (JacksonException e) {
            throw new IllegalStateException("Failed to serialize outbox payload for " + eventType, e);
        }
    }

    private record EventEnvelope(
            UUID eventId,
            String eventType,
            OffsetDateTime occurredAt,
            String producer,
            int version,
            Object data
    ) {
    }
}
