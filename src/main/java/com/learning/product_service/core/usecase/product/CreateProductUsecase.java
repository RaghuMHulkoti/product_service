package com.learning.product_service.core.usecase.product;

import com.learning.product_service.common.EventType;
import com.learning.product_service.core.domain.Product;
import com.learning.product_service.core.domain.ProductEventData;
import com.learning.product_service.core.domain.ProductStatus;
import com.learning.product_service.core.service.BrandService;
import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.service.OutboxService;
import com.learning.product_service.core.service.ProductService;
import com.learning.product_service.core.usecase.Usecase;
import com.learning.product_service.metrics.BusinessMetrics;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateProductUsecase implements Usecase<CreateProductUsecaseRequest, CreateProductUsecaseResponse> {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final OutboxService outboxService;
    private final BusinessMetrics businessMetrics;

    @Override
    @Transactional
    public CreateProductUsecaseResponse execute(CreateProductUsecaseRequest request) {
        var category = categoryService.getRequiredActive(request.getCategoryId());
        if (request.getBrandId() != null) {
            brandService.getRequired(request.getBrandId());
        }
        productService.ensureSkuIsFree(request.getSku());
        productService.ensureGtinIsFree(request.getGtin(), null);

        var attributes = request.getAttributes() != null ? request.getAttributes() : new HashMap<String, Object>();
        categoryService.validateAttributes(category, attributes);

        var product = productService.create(buildProduct(request, attributes));
        outboxService.append(product.getId(), EventType.PRODUCT_CREATED, ProductEventData.of(product, category));
        businessMetrics.productCreated(product.getStatus().name());

        return CreateProductUsecaseResponse.builder()
                .product(product)
                .build();
    }

    private Product buildProduct(CreateProductUsecaseRequest request, java.util.Map<String, Object> attributes) {
        return Product.builder()
                .id(UUID.randomUUID())
                .sku(request.getSku())
                .gtin(request.getGtin())
                .title(request.getTitle())
                .description(request.getDescription())
                .brandId(request.getBrandId())
                .categoryId(request.getCategoryId())
                .attributes(attributes)
                .mediaRoot(request.getMediaRoot())
                .status(request.getStatus() != null ? request.getStatus() : ProductStatus.DRAFT)
                .weightGrams(request.getWeightGrams())
                .dimLengthMm(request.getDimLengthMm())
                .dimWidthMm(request.getDimWidthMm())
                .dimHeightMm(request.getDimHeightMm())
                .hazardous(Boolean.TRUE.equals(request.getHazardous()))
                .build();
    }
}
