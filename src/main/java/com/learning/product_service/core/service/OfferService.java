package com.learning.product_service.core.service;

import com.learning.product_service.core.domain.OfferLookup;
import com.learning.product_service.gateway.SupplierOfferGateway;
import com.learning.product_service.metrics.BusinessMetrics;

import java.util.UUID;

public class OfferService {

    private final SupplierOfferGateway supplierOfferGateway;
    private final BusinessMetrics businessMetrics;

    public OfferService(SupplierOfferGateway supplierOfferGateway, BusinessMetrics businessMetrics) {
        this.supplierOfferGateway = supplierOfferGateway;
        this.businessMetrics = businessMetrics;
    }

    public OfferLookup activeOffers(UUID productId) {
        var lookup = supplierOfferGateway.findActiveOffers(productId);
        businessMetrics.supplierOfferLookup(lookup.available());
        return lookup;
    }
}
