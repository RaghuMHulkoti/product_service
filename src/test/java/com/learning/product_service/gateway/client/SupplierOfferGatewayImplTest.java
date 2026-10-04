package com.learning.product_service.gateway.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Pins the contract with Supplier Service: GET /api/v1/internal/offers?productId= returns a plain JSON array.
 */
class SupplierOfferGatewayImplTest {

    private static final UUID PRODUCT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final String URL = "http://supplier/api/v1/internal/offers?productId=" + PRODUCT_ID;

    private MockRestServiceServer server;
    private SupplierOfferGatewayImpl gateway;

    @BeforeEach
    void setUp() {
        var builder = RestClient.builder().baseUrl("http://supplier");
        server = MockRestServiceServer.bindTo(builder).build();
        gateway = new SupplierOfferGatewayImpl(builder.build());
    }

    @Test
    void parsesSupplierOfferArray() {
        server.expect(requestTo(URL)).andRespond(withSuccess("""
                [{"offerId":"22222222-2222-2222-2222-222222222222",
                  "supplierId":"33333333-3333-3333-3333-333333333333",
                  "productId":"11111111-1111-1111-1111-111111111111",
                  "supplierSku":"SUP-1","price":129999.0000,"currency":"INR","condition":"NEW",
                  "handlingDays":2,"active":true,"listedAt":"2026-10-01T10:00:00Z"}]
                """, MediaType.APPLICATION_JSON));

        var lookup = gateway.findActiveOffers(PRODUCT_ID);

        assertThat(lookup.available()).isTrue();
        assertThat(lookup.offers()).singleElement().satisfies(offer -> {
            assertThat(offer.supplierSku()).isEqualTo("SUP-1");
            assertThat(offer.price()).isEqualByComparingTo(new BigDecimal("129999"));
            assertThat(offer.condition()).isEqualTo("NEW");
            assertThat(offer.handlingDays()).isEqualTo((short) 2);
        });
        server.verify();
    }

    @Test
    void serverErrorDegradesToUnavailable() {
        server.expect(requestTo(URL)).andRespond(withServerError());

        var lookup = gateway.findActiveOffers(PRODUCT_ID);

        assertThat(lookup.available()).isFalse();
        assertThat(lookup.offers()).isEmpty();
    }

    @Test
    void connectionFailureDegradesToUnavailable() {
        var unreachable = new SupplierOfferGatewayImpl(RestClient.builder().baseUrl("http://localhost:1").build());

        var lookup = unreachable.findActiveOffers(PRODUCT_ID);

        assertThat(lookup.available()).isFalse();
    }
}
