package com.learning.product_service;

import com.jayway.jsonpath.JsonPath;
import com.learning.product_service.core.domain.Offer;
import com.learning.product_service.core.domain.OfferLookup;
import com.learning.product_service.gateway.SupplierOfferGateway;
import com.learning.product_service.gateway.repository.ProductOutboxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end tests for the catalog API: HTTP → processor → use case → service → JPA (H2).
 * Supplier Service is replaced by a mock gateway. Each test uses its own slugs and SKUs,
 * so tests do not depend on each other's data.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductOutboxRepository outboxRepository;

    @MockitoBean
    private SupplierOfferGateway supplierOfferGateway;

    private String suffix;
    private long rootCategoryId;
    private long phoneCategoryId;
    private long brandId;

    /** Builds: /electronics-x (requires color) → /smartphones-x (requires storage ENUM), plus one brand. */
    @BeforeEach
    void setUp() throws Exception {
        suffix = UUID.randomUUID().toString().substring(0, 8);
        rootCategoryId = createCategory(null, "electronics-" + suffix);
        phoneCategoryId = createCategory(rootCategoryId, "smartphones-" + suffix);
        post("/api/v1/categories/" + rootCategoryId + "/attributes",
                """
                {"attrKey":"color","dataType":"STRING","required":true}
                """).andExpect(status().isCreated());
        post("/api/v1/categories/" + phoneCategoryId + "/attributes",
                """
                {"attrKey":"storage","dataType":"ENUM","required":true,"facet":true,"enumValues":["128GB","256GB"]}
                """).andExpect(status().isCreated());
        brandId = idOf(post("/api/v1/brands", """
                {"slug":"apple-%s","name":"Apple"}
                """.formatted(suffix)).andExpect(status().isCreated()));
    }

    @Test
    void createProductWritesOutboxEventAndHasNoPriceOrQuantity() throws Exception {
        var productId = createActivePhone("IP15-" + suffix);

        mockMvc.perform(get("/api/v1/internal/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.product.sku").value("IP15-" + suffix))
                .andExpect(jsonPath("$.body.product.attributes.storage").value("256GB"))
                .andExpect(jsonPath("$.body.product.price").doesNotExist())
                .andExpect(jsonPath("$.body.product.quantity").doesNotExist())
                .andExpect(jsonPath("$.body.category.path").value("/electronics-%s/smartphones-%s".formatted(suffix, suffix)))
                .andExpect(jsonPath("$.body.brand.id").value(brandId))
                .andExpect(jsonPath("$.body.offers").value(nullValue()));
        verify(supplierOfferGateway, never()).findActiveOffers(any());

        var events = outboxRepository.findAllByAggregateIdOrderByIdAsc(UUID.fromString(productId));
        assertThat(events).singleElement().satisfies(event -> {
            assertThat(event.getEventType()).isEqualTo("PRODUCT_CREATED");
            assertThat(event.getStatus()).isEqualTo("PENDING");
            assertThat(event.getPayload()).contains("\"producer\":\"product-service\"", "\"categoryPath\":\"/electronics-");
        });
    }

    @Test
    void createProductRejectsAttributesThatBreakTheInheritedSchema() throws Exception {
        post("/api/v1/products", """
                {"sku":"BAD-%s","title":"Phone","categoryId":%d,"attributes":{"storage":"512GB"}}
                """.formatted(suffix, phoneCategoryId))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.status").value("FAILURE"))
                .andExpect(jsonPath("$.body.errorCode").value("BUSINESS_RULE_VIOLATION"))
                .andExpect(jsonPath("$.body.details.violation_1").value("Missing required attribute 'color'"))
                .andExpect(jsonPath("$.body.details.violation_2").value("Attribute 'storage' must be one of [128GB, 256GB]"));
    }

    @Test
    void createProductValidatesRequestBody() throws Exception {
        post("/api/v1/products", """
                {"sku":"has space","gtin":"12","title":"","categoryId":null}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.body.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.body.details.sku").exists())
                .andExpect(jsonPath("$.body.details.gtin").exists())
                .andExpect(jsonPath("$.body.details.title").exists())
                .andExpect(jsonPath("$.body.details.categoryId").exists());
    }

    @Test
    void createProductWithUnknownCategoryOrBrandIs404() throws Exception {
        post("/api/v1/products", """
                {"sku":"NOCAT-%s","title":"Phone","categoryId":999999}
                """.formatted(suffix))
                .andExpect(status().isNotFound());
        post("/api/v1/products", """
                {"sku":"NOBRAND-%s","title":"Phone","categoryId":%d,"brandId":999999,
                 "attributes":{"color":"Black","storage":"128GB"}}
                """.formatted(suffix, phoneCategoryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void duplicateSkuIsConflictButSkuIsReusableAfterSoftDelete() throws Exception {
        var sku = "DUP-" + suffix;
        var productId = createActivePhone(sku);

        post("/api/v1/products", phoneJson(sku, "ACTIVE")).andExpect(status().isConflict())
                .andExpect(jsonPath("$.body.errorCode").value("CONFLICT"));

        mockMvc.perform(delete("/api/v1/products/" + productId)).andExpect(status().isOk());

        post("/api/v1/products", phoneJson(sku, "ACTIVE")).andExpect(status().isCreated());
    }

    @Test
    void publicDetailIncludesOffersSortedByPrice() throws Exception {
        var productId = createActivePhone("OFR-" + suffix);
        var uuid = UUID.fromString(productId);
        when(supplierOfferGateway.findActiveOffers(uuid)).thenReturn(new OfferLookup(List.of(
                offer(uuid, "SUP-B", "134999.00"),
                offer(uuid, "SUP-A", "129999.00")
        ), true));

        mockMvc.perform(get("/api/v1/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.offersAvailable").value(true))
                .andExpect(jsonPath("$.body.offers", hasSize(2)))
                .andExpect(jsonPath("$.body.offers[0].supplierSku").value("SUP-A"))
                .andExpect(jsonPath("$.body.lowestOfferPrice").value(129999.00))
                .andExpect(jsonPath("$.body.lowestOfferCurrency").value("INR"))
                .andExpect(jsonPath("$.body.availableFromCount").value(2));
    }

    @Test
    void publicDetailStillWorksWhenSupplierServiceIsDown() throws Exception {
        var productId = createActivePhone("DOWN-" + suffix);
        when(supplierOfferGateway.findActiveOffers(any())).thenReturn(OfferLookup.unavailable());

        mockMvc.perform(get("/api/v1/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.product.id").value(productId))
                .andExpect(jsonPath("$.body.offersAvailable").value(false))
                .andExpect(jsonPath("$.body.offers", hasSize(0)));
    }

    @Test
    void draftProductIsHiddenFromPublicButVisibleInternally() throws Exception {
        String productId = idOf(post("/api/v1/products", phoneJson("DRAFT-" + suffix, null))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.body.status").value("DRAFT")));

        mockMvc.perform(get("/api/v1/products/" + productId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/v1/internal/products/" + productId)).andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/products").param("ids", productId))
                .andExpect(jsonPath("$.body.totalItems").value(0));
    }

    @Test
    void browseByParentSlugIncludesSubCategoryProducts() throws Exception {
        var productId = createActivePhone("BRW-" + suffix);

        mockMvc.perform(get("/api/v1/categories/electronics-" + suffix + "/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.items", hasSize(1)))
                .andExpect(jsonPath("$.body.items[0].id").value(productId));

        mockMvc.perform(get("/api/v1/categories/no-such-" + suffix + "/products"))
                .andExpect(status().isNotFound());
    }

    @Test
    void categoryTreeNestsChildren() throws Exception {
        var tree = mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Object> children = JsonPath.read(tree,
                "$.body[?(@.id == " + rootCategoryId + ")].children[*].slug");
        assertThat(children).containsExactly("smartphones-" + suffix);
    }

    @Test
    void listFiltersByIdsBrandAndQueryAndRejectsUnknownSortField() throws Exception {
        var first = createActivePhone("LST1-" + suffix);
        createActivePhone("LST2-" + suffix);

        mockMvc.perform(get("/api/v1/products").param("ids", first))
                .andExpect(jsonPath("$.body.items", hasSize(1)))
                .andExpect(jsonPath("$.body.items[0].id").value(first));
        mockMvc.perform(get("/api/v1/products").param("brandId", String.valueOf(brandId)).param("sort", "sku,asc"))
                .andExpect(jsonPath("$.body.totalItems").value(2))
                .andExpect(jsonPath("$.body.items[0].sku").value("LST1-" + suffix));
        mockMvc.perform(get("/api/v1/products").param("q", "IPHONE").param("brandId", String.valueOf(brandId)))
                .andExpect(jsonPath("$.body.totalItems").value(2));
        mockMvc.perform(get("/api/v1/products").param("q", "no-such-title-" + suffix))
                .andExpect(jsonPath("$.body.totalItems").value(0));
        mockMvc.perform(get("/api/v1/products").param("sort", "price,asc"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/v1/products").param("size", "500"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchUpdatesFieldsAndRejectsStaleVersion() throws Exception {
        var productId = createActivePhone("PATCH-" + suffix);

        patch(productId, """
                {"version":0,"title":"iPhone 15 Pro 256GB","attributes":{"color":"Blue","storage":"128GB"}}
                """)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.title").value("iPhone 15 Pro 256GB"))
                .andExpect(jsonPath("$.body.attributes.storage").value("128GB"))
                .andExpect(jsonPath("$.body.sku").value("PATCH-" + suffix))
                .andExpect(jsonPath("$.body.version").value(1));

        patch(productId, """
                {"version":0,"title":"stale write"}
                """)
                .andExpect(status().isConflict());

        patch(productId, """
                {"attributes":{"color":"Blue","storage":"1TB"}}
                """)
                .andExpect(status().isUnprocessableContent());

        var events = outboxRepository.findAllByAggregateIdOrderByIdAsc(UUID.fromString(productId));
        assertThat(events).extracting("eventType").containsExactly("PRODUCT_CREATED", "PRODUCT_UPDATED");
    }

    @Test
    void deleteIsSoftAndEmitsEvent() throws Exception {
        var productId = createActivePhone("DEL-" + suffix);

        mockMvc.perform(delete("/api/v1/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.body.productId").value(productId))
                .andExpect(jsonPath("$.body.deletedAt").exists());

        mockMvc.perform(get("/api/v1/products/" + productId)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/v1/internal/products/" + productId)).andExpect(status().isNotFound());
        mockMvc.perform(delete("/api/v1/products/" + productId)).andExpect(status().isNotFound());

        var events = outboxRepository.findAllByAggregateIdOrderByIdAsc(UUID.fromString(productId));
        assertThat(events).extracting("eventType").containsExactly("PRODUCT_CREATED", "PRODUCT_DELETED");
    }

    @Test
    void mediaIsAddedAndShownOnDetail() throws Exception {
        var productId = createActivePhone("MEDIA-" + suffix);
        when(supplierOfferGateway.findActiveOffers(any())).thenReturn(new OfferLookup(List.of(), true));

        post("/api/v1/products/" + productId + "/media", """
                {"kind":"IMAGE","url":"https://cdn.example.com/front.jpg","sortOrder":1,"altText":"Front"}
                """).andExpect(status().isCreated());
        post("/api/v1/products/" + productId + "/media", """
                {"kind":"IMAGE","url":"https://cdn.example.com/hero.jpg","sortOrder":0}
                """).andExpect(status().isCreated());
        post("/api/v1/products/" + productId + "/media", """
                {"kind":"IMAGE","url":"ftp://bad"}
                """).andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/products/" + productId))
                .andExpect(jsonPath("$.body.media", hasSize(2)))
                .andExpect(jsonPath("$.body.media[0].url").value("https://cdn.example.com/hero.jpg"));
    }

    @Test
    void duplicateSlugsAreConflicts() throws Exception {
        post("/api/v1/categories", """
                {"slug":"electronics-%s","name":"Again"}
                """.formatted(suffix)).andExpect(status().isConflict());
        post("/api/v1/brands", """
                {"slug":"apple-%s","name":"Again"}
                """.formatted(suffix)).andExpect(status().isConflict());
    }

    @Test
    void enumAttributeNeedsEnumValues() throws Exception {
        post("/api/v1/categories/" + phoneCategoryId + "/attributes", """
                {"attrKey":"network","dataType":"ENUM","required":false}
                """).andExpect(status().isUnprocessableContent());
    }

    @Test
    void openApiDocumentIsGenerated() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("Product Service API"))
                .andExpect(jsonPath("$.paths['/api/v1/products/{productId}']").exists());
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private String phoneJson(String sku, String status) {
        return """
                {"sku":"%s","title":"iPhone 15 Pro","categoryId":%d,"brandId":%d,%s
                 "attributes":{"color":"Natural","storage":"256GB"}}
                """.formatted(sku, phoneCategoryId, brandId, status == null ? "" : "\"status\":\"" + status + "\",");
    }

    private String createActivePhone(String sku) throws Exception {
        return JsonPath.read(post("/api/v1/products", phoneJson(sku, "ACTIVE"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString(), "$.body.id");
    }

    private long createCategory(Long parentId, String slug) throws Exception {
        return idOf(post("/api/v1/categories", """
                {"parentId":%s,"slug":"%s","name":"%s"}
                """.formatted(parentId, slug, slug)).andExpect(status().isCreated()));
    }

    private static <T> T idOf(ResultActions result) throws Exception {
        Object id = JsonPath.read(result.andReturn().getResponse().getContentAsString(), "$.body.id");
        @SuppressWarnings("unchecked")
        T value = (T) (id instanceof Integer i ? Long.valueOf(i) : id);
        return value;
    }

    private ResultActions post(String url, String json) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private ResultActions patch(String productId, String json) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/v1/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    private static Offer offer(UUID productId, String supplierSku, String price) {
        return new Offer(UUID.randomUUID(), UUID.randomUUID(), productId, supplierSku,
                new BigDecimal(price), "INR", "NEW", (short) 1, true, OffsetDateTime.now());
    }
}
