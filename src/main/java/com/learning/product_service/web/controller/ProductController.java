package com.learning.product_service.web.controller;

import com.learning.product_service.common.StatusCode;
import com.learning.product_service.core.domain.ProductFilter;
import com.learning.product_service.core.domain.ProductStatus;
import com.learning.product_service.core.processor.media.AddProductMediaProcessor;
import com.learning.product_service.core.processor.media.AddProductMediaProcessorRequest;
import com.learning.product_service.core.processor.product.CreateProductProcessor;
import com.learning.product_service.core.processor.product.CreateProductProcessorRequest;
import com.learning.product_service.core.processor.product.DeleteProductProcessor;
import com.learning.product_service.core.processor.product.DeleteProductProcessorRequest;
import com.learning.product_service.core.processor.product.GetProductProcessor;
import com.learning.product_service.core.processor.product.GetProductProcessorRequest;
import com.learning.product_service.core.processor.product.ListProductsProcessor;
import com.learning.product_service.core.processor.product.ListProductsProcessorRequest;
import com.learning.product_service.core.processor.product.UpdateProductProcessor;
import com.learning.product_service.core.processor.product.UpdateProductProcessorRequest;
import com.learning.product_service.web.mapper.PageableFactory;
import com.learning.product_service.web.mapper.ProductResponseMapper;
import com.learning.product_service.web.model.AddProductMediaRequest;
import com.learning.product_service.web.model.CreateProductRequest;
import com.learning.product_service.web.model.UpdateProductRequest;
import com.learning.product_service.web.response.DeleteProductResponse;
import com.learning.product_service.web.response.MediaResponse;
import com.learning.product_service.web.response.PageResponse;
import com.learning.product_service.web.response.ProductDetailResponse;
import com.learning.product_service.web.response.ProductResponse;
import com.learning.product_service.web.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Catalog product API. GET endpoints are public; POST/PATCH/DELETE are admin (enforced at the gateway).
 */
@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final CreateProductProcessor createProductProcessor;
    private final UpdateProductProcessor updateProductProcessor;
    private final DeleteProductProcessor deleteProductProcessor;
    private final GetProductProcessor getProductProcessor;
    private final ListProductsProcessor listProductsProcessor;
    private final AddProductMediaProcessor addProductMediaProcessor;

    /** Public list: only ACTIVE products. {@code ids} lets Search/Gateway hydrate a page of results. */
    @GetMapping
    public ResponseEntity<Response<PageResponse<ProductResponse>>> listProducts(
            @RequestParam(required = false) List<UUID> ids,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        var filter = ProductFilter.builder()
                .ids(ids)
                .categoryIds(categoryId == null ? null : List.of(categoryId))
                .brandId(brandId)
                .status(ProductStatus.ACTIVE)
                .query(q)
                .build();
        var result = listProductsProcessor.process(ListProductsProcessorRequest.builder()
                .filter(filter)
                .pageable(PageableFactory.of(page, size, sort))
                .build());
        return ok("Products retrieved successfully", ProductResponseMapper.toResponse(result.getPage()));
    }

    /** Public detail: catalog data plus active supplier offers. */
    @GetMapping("/{productId}")
    public ResponseEntity<Response<ProductDetailResponse>> getProduct(@PathVariable UUID productId) {
        var result = getProductProcessor.process(GetProductProcessorRequest.builder()
                .productId(productId)
                .publicView(true)
                .includeOffers(true)
                .build());
        return ok("Product retrieved successfully", ProductResponseMapper.toDetailResponse(result));
    }

    @PostMapping
    public ResponseEntity<Response<ProductResponse>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("Creating product sku={} categoryId={}", request.sku(), request.categoryId());
        var result = createProductProcessor.process(buildCreateProductProcessorRequest(request));
        return created("Product created successfully", ProductResponseMapper.toResponse(result.getProduct()));
    }

    private CreateProductProcessorRequest buildCreateProductProcessorRequest(CreateProductRequest request) {
        return CreateProductProcessorRequest.builder()
                .sku(request.sku())
                .gtin(request.gtin())
                .title(request.title())
                .description(request.description())
                .brandId(request.brandId())
                .categoryId(request.categoryId())
                .attributes(request.attributes())
                .mediaRoot(request.mediaRoot())
                .status(request.status())
                .weightGrams(request.weightGrams())
                .dimLengthMm(request.dimLengthMm())
                .dimWidthMm(request.dimWidthMm())
                .dimHeightMm(request.dimHeightMm())
                .hazardous(request.hazardous())
                .build();
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<Response<ProductResponse>> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        var result = updateProductProcessor.process(buildUpdateProductProcessorRequest(productId, request));
        return ok("Product updated successfully", ProductResponseMapper.toResponse(result.getProduct()));
    }

    private UpdateProductProcessorRequest buildUpdateProductProcessorRequest(UUID productId, UpdateProductRequest request) {
        return UpdateProductProcessorRequest.builder()
                .productId(productId)
                .expectedVersion(request.version())
                .gtin(request.gtin())
                .title(request.title())
                .description(request.description())
                .brandId(request.brandId())
                .categoryId(request.categoryId())
                .attributes(request.attributes())
                .mediaRoot(request.mediaRoot())
                .status(request.status())
                .weightGrams(request.weightGrams())
                .dimLengthMm(request.dimLengthMm())
                .dimWidthMm(request.dimWidthMm())
                .dimHeightMm(request.dimHeightMm())
                .hazardous(request.hazardous())
                .build();
    }

    /** Soft delete. */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Response<DeleteProductResponse>> deleteProduct(@PathVariable UUID productId) {
        log.info("Deleting product {}", productId);
        var result = deleteProductProcessor.process(DeleteProductProcessorRequest.builder()
                .productId(productId)
                .build());
        return ok("Product deleted successfully", new DeleteProductResponse(result.getProductId(), result.getDeletedAt()));
    }

    /**
     * Registers an uploaded media file. The upload itself goes straight to object storage
     * (pre-signed URL flow, not part of this service yet); this call stores the resulting URL.
     */
    @PostMapping("/{productId}/media")
    public ResponseEntity<Response<MediaResponse>> addMedia(
            @PathVariable UUID productId,
            @Valid @RequestBody AddProductMediaRequest request
    ) {
        var result = addProductMediaProcessor.process(AddProductMediaProcessorRequest.builder()
                .productId(productId)
                .kind(request.kind())
                .url(request.url())
                .sortOrder(request.sortOrder())
                .altText(request.altText())
                .build());
        return created("Media added successfully", ProductResponseMapper.toResponse(result.getMedia()));
    }

    private static <T> ResponseEntity<Response<T>> ok(String message, T body) {
        return ResponseEntity.ok(new Response<>(StatusCode.SUCCESS, message, body));
    }

    private static <T> ResponseEntity<Response<T>> created(String message, T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(StatusCode.SUCCESS, message, body));
    }
}
