package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.product.UpdateProductUsecase;
import com.learning.product_service.core.usecase.product.UpdateProductUsecaseRequest;
import com.learning.product_service.core.usecase.product.UpdateProductUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateProductProcessor implements Processor<UpdateProductProcessorRequest, UpdateProductProcessorResponse> {

    private final UpdateProductUsecase updateProductUsecase;

    @Override
    public UpdateProductProcessorResponse process(UpdateProductProcessorRequest request) {
        var usecaseRequest = buildUpdateProductUsecaseRequest(request);
        var usecaseResponse = updateProductUsecase.execute(usecaseRequest);
        return buildUpdateProductProcessorResponse(usecaseResponse);
    }

    private UpdateProductUsecaseRequest buildUpdateProductUsecaseRequest(UpdateProductProcessorRequest request) {
        return UpdateProductUsecaseRequest.builder()
                .productId(request.getProductId())
                .expectedVersion(request.getExpectedVersion())
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

    private UpdateProductProcessorResponse buildUpdateProductProcessorResponse(UpdateProductUsecaseResponse usecaseResponse) {
        return UpdateProductProcessorResponse.builder()
                .product(usecaseResponse.getProduct())
                .build();
    }
}
