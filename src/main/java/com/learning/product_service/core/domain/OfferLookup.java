package com.learning.product_service.core.domain;

import java.util.List;

/**
 * Result of asking Supplier Service for a product's active offers.
 * {@code available = false} means Supplier Service could not be reached and {@code offers} is empty.
 */
public record OfferLookup(List<Offer> offers, boolean available) {

    public static OfferLookup unavailable() {
        return new OfferLookup(List.of(), false);
    }
}
