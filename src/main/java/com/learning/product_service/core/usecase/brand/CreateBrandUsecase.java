package com.learning.product_service.core.usecase.brand;

import com.learning.product_service.core.service.BrandService;
import com.learning.product_service.core.usecase.Usecase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CreateBrandUsecase implements Usecase<CreateBrandUsecaseRequest, CreateBrandUsecaseResponse> {

    private final BrandService brandService;

    @Override
    @Transactional
    public CreateBrandUsecaseResponse execute(CreateBrandUsecaseRequest request) {
        return CreateBrandUsecaseResponse.builder()
                .brand(brandService.create(request.getSlug(), request.getName()))
                .build();
    }
}
