package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.product.ListProductsUsecase;
import com.learning.product_service.core.usecase.product.ListProductsUsecaseRequest;
import com.learning.product_service.core.usecase.product.ListProductsUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListProductsProcessor implements Processor<ListProductsProcessorRequest, ListProductsProcessorResponse> {

    private final ListProductsUsecase listProductsUsecase;

    @Override
    public ListProductsProcessorResponse process(ListProductsProcessorRequest request) {
        var usecaseRequest = buildListProductsUsecaseRequest(request);
        var usecaseResponse = listProductsUsecase.execute(usecaseRequest);
        return buildListProductsProcessorResponse(usecaseResponse);
    }

    private ListProductsUsecaseRequest buildListProductsUsecaseRequest(ListProductsProcessorRequest request) {
        return ListProductsUsecaseRequest.builder()
                .filter(request.getFilter())
                .categorySlug(request.getCategorySlug())
                .pageable(request.getPageable())
                .build();
    }

    private ListProductsProcessorResponse buildListProductsProcessorResponse(ListProductsUsecaseResponse usecaseResponse) {
        return ListProductsProcessorResponse.builder()
                .page(usecaseResponse.getPage())
                .build();
    }
}
