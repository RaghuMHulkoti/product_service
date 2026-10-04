package com.learning.product_service.core.usecase.product;

import com.learning.product_service.core.service.BrandService;
import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.service.OfferService;
import com.learning.product_service.core.service.ProductMediaService;
import com.learning.product_service.core.service.ProductService;
import com.learning.product_service.core.usecase.Usecase;
import lombok.RequiredArgsConstructor;

/**
 * Product detail. No surrounding transaction on purpose: the Supplier Service call must not
 * hold a database connection open.
 */
@RequiredArgsConstructor
public class GetProductUsecase implements Usecase<GetProductUsecaseRequest, GetProductUsecaseResponse> {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductMediaService productMediaService;
    private final OfferService offerService;

    @Override
    public GetProductUsecaseResponse execute(GetProductUsecaseRequest request) {
        var product = Boolean.TRUE.equals(request.getPublicView())
                ? productService.getPubliclyVisible(request.getProductId())
                : productService.getRequired(request.getProductId());

        return GetProductUsecaseResponse.builder()
                .product(product)
                .category(categoryService.getRequired(product.getCategoryId()))
                .brand(brandService.findOrNull(product.getBrandId()))
                .media(productMediaService.list(product.getId()))
                .offerLookup(Boolean.TRUE.equals(request.getIncludeOffers())
                        ? offerService.activeOffers(product.getId())
                        : null)
                .build();
    }
}
