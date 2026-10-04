package com.learning.product_service.web.controller;

import com.learning.product_service.common.StatusCode;
import com.learning.product_service.core.domain.ProductFilter;
import com.learning.product_service.core.domain.ProductStatus;
import com.learning.product_service.core.processor.category.CreateCategoryAttributeProcessor;
import com.learning.product_service.core.processor.category.CreateCategoryAttributeProcessorRequest;
import com.learning.product_service.core.processor.category.CreateCategoryProcessor;
import com.learning.product_service.core.processor.category.CreateCategoryProcessorRequest;
import com.learning.product_service.core.processor.category.GetCategoryTreeProcessor;
import com.learning.product_service.core.processor.category.GetCategoryTreeProcessorRequest;
import com.learning.product_service.core.processor.product.ListProductsProcessor;
import com.learning.product_service.core.processor.product.ListProductsProcessorRequest;
import com.learning.product_service.web.mapper.PageableFactory;
import com.learning.product_service.web.mapper.ProductResponseMapper;
import com.learning.product_service.web.model.CreateCategoryAttributeRequest;
import com.learning.product_service.web.model.CreateCategoryRequest;
import com.learning.product_service.web.response.CategoryAttributeResponse;
import com.learning.product_service.web.response.CategoryResponse;
import com.learning.product_service.web.response.PageResponse;
import com.learning.product_service.web.response.ProductResponse;
import com.learning.product_service.web.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final GetCategoryTreeProcessor getCategoryTreeProcessor;
    private final CreateCategoryProcessor createCategoryProcessor;
    private final CreateCategoryAttributeProcessor createCategoryAttributeProcessor;
    private final ListProductsProcessor listProductsProcessor;

    /** Public: active categories as a tree. */
    @GetMapping
    public ResponseEntity<Response<List<CategoryResponse>>> getCategoryTree() {
        var result = getCategoryTreeProcessor.process(new GetCategoryTreeProcessorRequest());
        return ok("Categories retrieved successfully", ProductResponseMapper.toResponse(result.getRoots()));
    }

    /** Public browse: ACTIVE products in this category and all its sub-categories. */
    @GetMapping("/{slug}/products")
    public ResponseEntity<Response<PageResponse<ProductResponse>>> browseCategory(
            @PathVariable String slug,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "24") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        var result = listProductsProcessor.process(ListProductsProcessorRequest.builder()
                .categorySlug(slug)
                .filter(ProductFilter.builder()
                        .brandId(brandId)
                        .status(ProductStatus.ACTIVE)
                        .query(q)
                        .build())
                .pageable(PageableFactory.of(page, size, sort))
                .build());
        return ok("Products retrieved successfully", ProductResponseMapper.toResponse(result.getPage()));
    }

    /** Admin. */
    @PostMapping
    public ResponseEntity<Response<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        var result = createCategoryProcessor.process(CreateCategoryProcessorRequest.builder()
                .parentId(request.parentId())
                .slug(request.slug())
                .name(request.name())
                .build());
        return created("Category created successfully", ProductResponseMapper.toResponse(result.getCategory()));
    }

    /** Admin: define (or redefine) an attribute that products in this category carry. */
    @PostMapping("/{categoryId}/attributes")
    public ResponseEntity<Response<CategoryAttributeResponse>> createCategoryAttribute(
            @PathVariable Long categoryId,
            @Valid @RequestBody CreateCategoryAttributeRequest request
    ) {
        var result = createCategoryAttributeProcessor.process(CreateCategoryAttributeProcessorRequest.builder()
                .categoryId(categoryId)
                .attrKey(request.attrKey())
                .dataType(request.dataType())
                .required(request.required())
                .facet(request.facet())
                .enumValues(request.enumValues())
                .build());
        return created("Category attribute saved successfully", ProductResponseMapper.toResponse(result.getAttribute()));
    }

    private static <T> ResponseEntity<Response<T>> ok(String message, T body) {
        return ResponseEntity.ok(new Response<>(StatusCode.SUCCESS, message, body));
    }

    private static <T> ResponseEntity<Response<T>> created(String message, T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(StatusCode.SUCCESS, message, body));
    }
}
