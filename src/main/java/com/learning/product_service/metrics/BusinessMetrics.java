package com.learning.product_service.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

/**
 * Domain counters that the generic layer aspect cannot derive on its own.
 * <ul>
 *   <li>{@code product.created} tagged by initial {@code status}</li>
 *   <li>{@code product.outbox.events} tagged by {@code eventType}</li>
 *   <li>{@code product.supplier.offer.lookups} tagged by {@code outcome} (available / unavailable)</li>
 * </ul>
 */
@Component
public class BusinessMetrics {

    private final MeterRegistry registry;

    public BusinessMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    public void productCreated(String status) {
        registry.counter("product.created", "status", status).increment();
    }

    public void outboxEventAppended(String eventType) {
        registry.counter("product.outbox.events", "eventType", eventType).increment();
    }

    public void supplierOfferLookup(boolean available) {
        registry.counter("product.supplier.offer.lookups", "outcome", available ? "available" : "unavailable")
                .increment();
    }
}
