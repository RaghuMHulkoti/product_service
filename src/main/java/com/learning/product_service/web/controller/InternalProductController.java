package com.learning.product_service.web.controller;

import com.learning.product_service.common.StatusCode;
import com.learning.product_service.core.domain.ProductFilter;
import com.learning.product_service.core.domain.ProductStatus;
import com.learning.product_service.core.processor.product.GetProductProcessor;
import com.learning.product_service.core.processor.product.GetProductProcessorRequest;
import com.learning.product_service.core.processor.product.ListProductsProcessor;
import com.learning.product_service.core.processor.product.ListProductsProcessorRequest;
import com.learning.product_service.web.mapper.PageableFactory;
import com.learning.product_service.web.mapper.ProductResponseMapper;
import com.learning.product_service.web.response.PageResponse;
import com.learning.product_service.web.response.ProductDetailResponse;
import com.learning.product_service.web.response.ProductResponse;
import com.learning.product_service.web.response.Response;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Service-to-service API (mTLS at the mesh). Sees products in every status except deleted,
 * and never calls Supplier Service, so it stays fast for Cart, Order and Supplier lookups.
 */
@Validated
@RestController
@RequestMapping("/api/v1/internal/products")
@RequiredArgsConstructor
public class InternalProductController {

    private final GetProductProcessor getProductProcessor;
    private final ListProductsProcessor listProductsProcessor;

    @GetMapping("/{productId}")
    public ResponseEntity<Response<ProductDetailResponse>> getProduct(@PathVariable UUID productId) {
        var result = getProductProcessor.process(GetProductProcessorRequest.builder()
                .productId(productId)
                .publicView(false)
                .includeOffers(false)
                .build());
        return ResponseEntity.ok(new Response<>(StatusCode.SUCCESS, "Product retrieved successfully",
                ProductResponseMapper.toDetailResponse(result)));
    }

    @GetMapping
    public ResponseEntity<Response<PageResponse<ProductResponse>>> listProducts(
            @RequestParam(required = false) List<UUID> ids,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        var result = listProductsProcessor.process(ListProductsProcessorRequest.builder()
                .filter(ProductFilter.builder()
                        .ids(ids)
                        .categoryIds(categoryId == null ? null : List.of(categoryId))
                        .brandId(brandId)
                        .status(status)
                        .query(q)
                        .build())
                .pageable(PageableFactory.of(page, size, sort))
                .build());
        return ResponseEntity.ok(new Response<>(StatusCode.SUCCESS, "Products retrieved successfully",
                ProductResponseMapper.toResponse(result.getPage())));
    }
}
