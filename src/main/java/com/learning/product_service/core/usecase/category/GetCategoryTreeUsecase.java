package com.learning.product_service.core.usecase.category;

import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.usecase.Usecase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class GetCategoryTreeUsecase implements Usecase<GetCategoryTreeUsecaseRequest, GetCategoryTreeUsecaseResponse> {

    private final CategoryService categoryService;

    @Override
    @Transactional(readOnly = true)
    public GetCategoryTreeUsecaseResponse execute(GetCategoryTreeUsecaseRequest request) {
        return GetCategoryTreeUsecaseResponse.builder()
                .roots(categoryService.tree())
                .build();
    }
}
