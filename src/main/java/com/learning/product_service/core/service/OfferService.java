package com.learning.product_service.core.service;

import com.learning.product_service.core.domain.OfferLookup;
import com.learning.product_service.gateway.SupplierOfferGateway;

import java.util.UUID;

public class OfferService {

    private final SupplierOfferGateway supplierOfferGateway;

    public OfferService(SupplierOfferGateway supplierOfferGateway) {
        this.supplierOfferGateway = supplierOfferGateway;
    }

    public OfferLookup activeOffers(UUID productId) {
        return supplierOfferGateway.findActiveOffers(productId);
    }
}
