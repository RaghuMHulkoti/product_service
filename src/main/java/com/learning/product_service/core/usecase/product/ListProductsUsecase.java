package com.learning.product_service.core.usecase.product;

import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.service.ProductService;
import com.learning.product_service.core.usecase.Usecase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ListProductsUsecase implements Usecase<ListProductsUsecaseRequest, ListProductsUsecaseResponse> {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Override
    @Transactional(readOnly = true)
    public ListProductsUsecaseResponse execute(ListProductsUsecaseRequest request) {
        var filter = request.getFilter();
        if (request.getCategorySlug() != null) {
            var category = categoryService.getRequiredBySlug(request.getCategorySlug());
            filter.setCategoryIds(categoryService.subtreeIds(category));
        }
        return ListProductsUsecaseResponse.builder()
                .page(productService.search(filter, request.getPageable()))
                .build();
    }
}
