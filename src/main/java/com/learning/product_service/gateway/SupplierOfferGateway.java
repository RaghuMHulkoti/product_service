package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.OfferLookup;

import java.util.UUID;

/**
 * Reads supplier offers (price lives there, not in the catalog).
 */
public interface SupplierOfferGateway {

    /** Never throws for transport errors: returns {@link OfferLookup#unavailable()} instead. */
    OfferLookup findActiveOffers(UUID productId);
}
