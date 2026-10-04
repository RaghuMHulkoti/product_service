package com.learning.product_service.common;

/**
 * Catalog events written to the product outbox. Search, Cart and Analytics consume them.
 */
public enum EventType {
    PRODUCT_CREATED,
    PRODUCT_UPDATED,
    PRODUCT_DELETED
}
