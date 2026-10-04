package com.learning.product_service.core.usecase.brand;

import com.learning.product_service.core.service.BrandService;
import com.learning.product_service.core.usecase.Usecase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ListBrandsUsecase implements Usecase<ListBrandsUsecaseRequest, ListBrandsUsecaseResponse> {

    private final BrandService brandService;

    @Override
    @Transactional(readOnly = true)
    public ListBrandsUsecaseResponse execute(ListBrandsUsecaseRequest request) {
        return ListBrandsUsecaseResponse.builder()
                .brands(brandService.list())
                .build();
    }
}
