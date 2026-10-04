package com.learning.product_service.core.processor.category;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.category.CreateCategoryAttributeUsecase;
import com.learning.product_service.core.usecase.category.CreateCategoryAttributeUsecaseRequest;
import com.learning.product_service.core.usecase.category.CreateCategoryAttributeUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateCategoryAttributeProcessor implements Processor<CreateCategoryAttributeProcessorRequest, CreateCategoryAttributeProcessorResponse> {

    private final CreateCategoryAttributeUsecase createCategoryAttributeUsecase;

    @Override
    public CreateCategoryAttributeProcessorResponse process(CreateCategoryAttributeProcessorRequest request) {
        var usecaseRequest = buildCreateCategoryAttributeUsecaseRequest(request);
        var usecaseResponse = createCategoryAttributeUsecase.execute(usecaseRequest);
        return buildCreateCategoryAttributeProcessorResponse(usecaseResponse);
    }

    private CreateCategoryAttributeUsecaseRequest buildCreateCategoryAttributeUsecaseRequest(CreateCategoryAttributeProcessorRequest request) {
        return CreateCategoryAttributeUsecaseRequest.builder()
                .categoryId(request.getCategoryId())
                .attrKey(request.getAttrKey())
                .dataType(request.getDataType())
                .required(request.getRequired())
                .facet(request.getFacet())
                .enumValues(request.getEnumValues())
                .build();
    }

    private CreateCategoryAttributeProcessorResponse buildCreateCategoryAttributeProcessorResponse(CreateCategoryAttributeUsecaseResponse usecaseResponse) {
        return CreateCategoryAttributeProcessorResponse.builder()
                .attribute(usecaseResponse.getAttribute())
                .build();
    }
}
