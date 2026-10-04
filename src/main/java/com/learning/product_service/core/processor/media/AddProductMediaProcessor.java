package com.learning.product_service.core.processor.media;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.media.AddProductMediaUsecase;
import com.learning.product_service.core.usecase.media.AddProductMediaUsecaseRequest;
import com.learning.product_service.core.usecase.media.AddProductMediaUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddProductMediaProcessor implements Processor<AddProductMediaProcessorRequest, AddProductMediaProcessorResponse> {

    private final AddProductMediaUsecase addProductMediaUsecase;

    @Override
    public AddProductMediaProcessorResponse process(AddProductMediaProcessorRequest request) {
        var usecaseRequest = buildAddProductMediaUsecaseRequest(request);
        var usecaseResponse = addProductMediaUsecase.execute(usecaseRequest);
        return buildAddProductMediaProcessorResponse(usecaseResponse);
    }

    private AddProductMediaUsecaseRequest buildAddProductMediaUsecaseRequest(AddProductMediaProcessorRequest request) {
        return AddProductMediaUsecaseRequest.builder()
                .productId(request.getProductId())
                .kind(request.getKind())
                .url(request.getUrl())
                .sortOrder(request.getSortOrder())
                .altText(request.getAltText())
                .build();
    }

    private AddProductMediaProcessorResponse buildAddProductMediaProcessorResponse(AddProductMediaUsecaseResponse usecaseResponse) {
        return AddProductMediaProcessorResponse.builder()
                .media(usecaseResponse.getMedia())
                .build();
    }
}
