package com.learning.product_service.core.processor.category;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.category.CreateCategoryUsecase;
import com.learning.product_service.core.usecase.category.CreateCategoryUsecaseRequest;
import com.learning.product_service.core.usecase.category.CreateCategoryUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCategoryProcessor implements Processor<CreateCategoryProcessorRequest, CreateCategoryProcessorResponse> {

    private final CreateCategoryUsecase createCategoryUsecase;

    @Override
    public CreateCategoryProcessorResponse process(CreateCategoryProcessorRequest request) {
        var usecaseRequest = buildCreateCategoryUsecaseRequest(request);
        var usecaseResponse = createCategoryUsecase.execute(usecaseRequest);
        return buildCreateCategoryProcessorResponse(usecaseResponse);
    }

    private CreateCategoryUsecaseRequest buildCreateCategoryUsecaseRequest(CreateCategoryProcessorRequest request) {
        return CreateCategoryUsecaseRequest.builder()
                .parentId(request.getParentId())
                .slug(request.getSlug())
                .name(request.getName())
                .build();
    }

    private CreateCategoryProcessorResponse buildCreateCategoryProcessorResponse(CreateCategoryUsecaseResponse usecaseResponse) {
        return CreateCategoryProcessorResponse.builder()
                .category(usecaseResponse.getCategory())
                .build();
    }
}
