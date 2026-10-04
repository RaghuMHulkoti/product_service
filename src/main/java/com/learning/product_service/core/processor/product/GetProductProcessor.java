package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.product.GetProductUsecase;
import com.learning.product_service.core.usecase.product.GetProductUsecaseRequest;
import com.learning.product_service.core.usecase.product.GetProductUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetProductProcessor implements Processor<GetProductProcessorRequest, GetProductProcessorResponse> {

    private final GetProductUsecase getProductUsecase;

    @Override
    public GetProductProcessorResponse process(GetProductProcessorRequest request) {
        var usecaseRequest = buildGetProductUsecaseRequest(request);
        var usecaseResponse = getProductUsecase.execute(usecaseRequest);
        return buildGetProductProcessorResponse(usecaseResponse);
    }

    private GetProductUsecaseRequest buildGetProductUsecaseRequest(GetProductProcessorRequest request) {
        return GetProductUsecaseRequest.builder()
                .productId(request.getProductId())
                .publicView(request.getPublicView())
                .includeOffers(request.getIncludeOffers())
                .build();
    }

    private GetProductProcessorResponse buildGetProductProcessorResponse(GetProductUsecaseResponse usecaseResponse) {
        return GetProductProcessorResponse.builder()
                .product(usecaseResponse.getProduct())
                .brand(usecaseResponse.getBrand())
                .category(usecaseResponse.getCategory())
                .media(usecaseResponse.getMedia())
                .offerLookup(usecaseResponse.getOfferLookup())
                .build();
    }
}
