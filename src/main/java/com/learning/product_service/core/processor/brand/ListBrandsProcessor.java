package com.learning.product_service.core.processor.brand;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.brand.ListBrandsUsecase;
import com.learning.product_service.core.usecase.brand.ListBrandsUsecaseRequest;
import com.learning.product_service.core.usecase.brand.ListBrandsUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListBrandsProcessor implements Processor<ListBrandsProcessorRequest, ListBrandsProcessorResponse> {

    private final ListBrandsUsecase listBrandsUsecase;

    @Override
    public ListBrandsProcessorResponse process(ListBrandsProcessorRequest request) {
        var usecaseRequest = buildListBrandsUsecaseRequest(request);
        var usecaseResponse = listBrandsUsecase.execute(usecaseRequest);
        return buildListBrandsProcessorResponse(usecaseResponse);
    }

    private ListBrandsUsecaseRequest buildListBrandsUsecaseRequest(ListBrandsProcessorRequest request) {
        return ListBrandsUsecaseRequest.builder()
                .build();
    }

    private ListBrandsProcessorResponse buildListBrandsProcessorResponse(ListBrandsUsecaseResponse usecaseResponse) {
        return ListBrandsProcessorResponse.builder()
                .brands(usecaseResponse.getBrands())
                .build();
    }
}
