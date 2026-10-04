package com.learning.product_service.web.controller;

import com.learning.product_service.common.StatusCode;
import com.learning.product_service.core.processor.brand.CreateBrandProcessor;
import com.learning.product_service.core.processor.brand.CreateBrandProcessorRequest;
import com.learning.product_service.core.processor.brand.ListBrandsProcessor;
import com.learning.product_service.core.processor.brand.ListBrandsProcessorRequest;
import com.learning.product_service.web.mapper.ProductResponseMapper;
import com.learning.product_service.web.model.CreateBrandRequest;
import com.learning.product_service.web.response.BrandResponse;
import com.learning.product_service.web.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final ListBrandsProcessor listBrandsProcessor;
    private final CreateBrandProcessor createBrandProcessor;

    @GetMapping
    public ResponseEntity<Response<List<BrandResponse>>> listBrands() {
        var result = listBrandsProcessor.process(new ListBrandsProcessorRequest());
        var brands = result.getBrands().stream().map(ProductResponseMapper::toResponse).toList();
        return ResponseEntity.ok(new Response<>(StatusCode.SUCCESS, "Brands retrieved successfully", brands));
    }

    /** Admin. */
    @PostMapping
    public ResponseEntity<Response<BrandResponse>> createBrand(@Valid @RequestBody CreateBrandRequest request) {
        var result = createBrandProcessor.process(CreateBrandProcessorRequest.builder()
                .slug(request.slug())
                .name(request.name())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response<>(StatusCode.SUCCESS,
                "Brand created successfully", ProductResponseMapper.toResponse(result.getBrand())));
    }
}
