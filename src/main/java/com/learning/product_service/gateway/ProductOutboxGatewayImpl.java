package com.learning.product_service.gateway;

import com.learning.product_service.gateway.entity.ProductOutboxEntity;
import com.learning.product_service.gateway.repository.ProductOutboxRepository;

import java.util.UUID;

public class ProductOutboxGatewayImpl implements ProductOutboxGateway {

    private final ProductOutboxRepository repository;

    public ProductOutboxGatewayImpl(ProductOutboxRepository repository) {
        this.repository = repository;
    }

    @Override
    public void append(UUID aggregateId, String eventType, String payload) {
        repository.save(ProductOutboxEntity.builder()
                .aggregateId(aggregateId)
                .eventType(eventType)
                .payload(payload)
                .build());
    }
}
