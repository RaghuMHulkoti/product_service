package com.learning.product_service.core.processor.category;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.category.GetCategoryTreeUsecase;
import com.learning.product_service.core.usecase.category.GetCategoryTreeUsecaseRequest;
import com.learning.product_service.core.usecase.category.GetCategoryTreeUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetCategoryTreeProcessor implements Processor<GetCategoryTreeProcessorRequest, GetCategoryTreeProcessorResponse> {

    private final GetCategoryTreeUsecase getCategoryTreeUsecase;

    @Override
    public GetCategoryTreeProcessorResponse process(GetCategoryTreeProcessorRequest request) {
        var usecaseRequest = buildGetCategoryTreeUsecaseRequest(request);
        var usecaseResponse = getCategoryTreeUsecase.execute(usecaseRequest);
        return buildGetCategoryTreeProcessorResponse(usecaseResponse);
    }

    private GetCategoryTreeUsecaseRequest buildGetCategoryTreeUsecaseRequest(GetCategoryTreeProcessorRequest request) {
        return GetCategoryTreeUsecaseRequest.builder()
                .build();
    }

    private GetCategoryTreeProcessorResponse buildGetCategoryTreeProcessorResponse(GetCategoryTreeUsecaseResponse usecaseResponse) {
        return GetCategoryTreeProcessorResponse.builder()
                .roots(usecaseResponse.getRoots())
                .build();
    }
}
