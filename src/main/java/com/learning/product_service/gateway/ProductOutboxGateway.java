package com.learning.product_service.gateway;

import java.util.UUID;

public interface ProductOutboxGateway {

    /** Stores a PENDING event. Must run inside the same transaction as the state change. */
    void append(UUID aggregateId, String eventType, String payload);
}
