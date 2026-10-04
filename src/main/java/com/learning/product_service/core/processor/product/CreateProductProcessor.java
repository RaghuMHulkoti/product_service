package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.product.CreateProductUsecase;
import com.learning.product_service.core.usecase.product.CreateProductUsecaseRequest;
import com.learning.product_service.core.usecase.product.CreateProductUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateProductProcessor implements Processor<CreateProductProcessorRequest, CreateProductProcessorResponse> {

    private final CreateProductUsecase createProductUsecase;

    @Override
    public CreateProductProcessorResponse process(CreateProductProcessorRequest request) {
        var usecaseRequest = buildCreateProductUsecaseRequest(request);
        var usecaseResponse = createProductUsecase.execute(usecaseRequest);
        return buildCreateProductProcessorResponse(usecaseResponse);
    }

    private CreateProductUsecaseRequest buildCreateProductUsecaseRequest(CreateProductProcessorRequest request) {
        return CreateProductUsecaseRequest.builder()
                .sku(request.getSku())
                .gtin(request.getGtin())
                .title(request.getTitle())
                .description(request.getDescription())
                .brandId(request.getBrandId())
                .categoryId(request.getCategoryId())
                .attributes(request.getAttributes())
                .mediaRoot(request.getMediaRoot())
                .status(request.getStatus())
                .weightGrams(request.getWeightGrams())
                .dimLengthMm(request.getDimLengthMm())
                .dimWidthMm(request.getDimWidthMm())
                .dimHeightMm(request.getDimHeightMm())
                .hazardous(request.getHazardous())
                .build();
    }

    private CreateProductProcessorResponse buildCreateProductProcessorResponse(CreateProductUsecaseResponse usecaseResponse) {
        return CreateProductProcessorResponse.builder()
                .product(usecaseResponse.getProduct())
                .build();
    }
}
