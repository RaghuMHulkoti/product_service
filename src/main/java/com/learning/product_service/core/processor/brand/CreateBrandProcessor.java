package com.learning.product_service.core.processor.brand;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.brand.CreateBrandUsecase;
import com.learning.product_service.core.usecase.brand.CreateBrandUsecaseRequest;
import com.learning.product_service.core.usecase.brand.CreateBrandUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateBrandProcessor implements Processor<CreateBrandProcessorRequest, CreateBrandProcessorResponse> {

    private final CreateBrandUsecase createBrandUsecase;

    @Override
    public CreateBrandProcessorResponse process(CreateBrandProcessorRequest request) {
        var usecaseRequest = buildCreateBrandUsecaseRequest(request);
        var usecaseResponse = createBrandUsecase.execute(usecaseRequest);
        return buildCreateBrandProcessorResponse(usecaseResponse);
    }

    private CreateBrandUsecaseRequest buildCreateBrandUsecaseRequest(CreateBrandProcessorRequest request) {
        return CreateBrandUsecaseRequest.builder()
                .slug(request.getSlug())
                .name(request.getName())
                .build();
    }

    private CreateBrandProcessorResponse buildCreateBrandProcessorResponse(CreateBrandUsecaseResponse usecaseResponse) {
        return CreateBrandProcessorResponse.builder()
                .brand(usecaseResponse.getBrand())
                .build();
    }
}
