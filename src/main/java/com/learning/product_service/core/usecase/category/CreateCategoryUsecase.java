package com.learning.product_service.core.usecase.category;

import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.usecase.Usecase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CreateCategoryUsecase implements Usecase<CreateCategoryUsecaseRequest, CreateCategoryUsecaseResponse> {

    private final CategoryService categoryService;

    @Override
    @Transactional
    public CreateCategoryUsecaseResponse execute(CreateCategoryUsecaseRequest request) {
        return CreateCategoryUsecaseResponse.builder()
                .category(categoryService.create(request.getParentId(), request.getSlug(), request.getName()))
                .build();
    }
}
