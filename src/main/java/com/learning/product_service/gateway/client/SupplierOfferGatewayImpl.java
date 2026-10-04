package com.learning.product_service.gateway.client;

import com.learning.product_service.core.domain.Offer;
import com.learning.product_service.core.domain.OfferLookup;
import com.learning.product_service.gateway.SupplierOfferGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.UUID;

/**
 * Calls Supplier Service {@code GET /api/v1/internal/offers?productId=}, which returns a plain JSON
 * array of offers sorted by price. Failures degrade to "offers unavailable" so product detail still renders.
 */
public class SupplierOfferGatewayImpl implements SupplierOfferGateway {

    private static final Logger log = LoggerFactory.getLogger(SupplierOfferGatewayImpl.class);
    private static final ParameterizedTypeReference<List<Offer>> OFFER_LIST = new ParameterizedTypeReference<>() {};

    private final RestClient supplierRestClient;

    public SupplierOfferGatewayImpl(RestClient supplierRestClient) {
        this.supplierRestClient = supplierRestClient;
    }

    @Override
    public OfferLookup findActiveOffers(UUID productId) {
        try {
            var offers = supplierRestClient.get()
                    .uri(uri -> uri.path("/api/v1/internal/offers").queryParam("productId", productId).build())
                    .retrieve()
                    .body(OFFER_LIST);
            return new OfferLookup(offers != null ? offers : List.of(), true);
        } catch (RestClientException e) {
            log.warn("Supplier Service unavailable for product {}: {}", productId, e.getMessage());
            return OfferLookup.unavailable();
        }
    }
}
