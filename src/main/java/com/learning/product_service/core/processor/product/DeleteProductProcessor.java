package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.Processor;
import com.learning.product_service.core.usecase.product.DeleteProductUsecase;
import com.learning.product_service.core.usecase.product.DeleteProductUsecaseRequest;
import com.learning.product_service.core.usecase.product.DeleteProductUsecaseResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteProductProcessor implements Processor<DeleteProductProcessorRequest, DeleteProductProcessorResponse> {

    private final DeleteProductUsecase deleteProductUsecase;

    @Override
    public DeleteProductProcessorResponse process(DeleteProductProcessorRequest request) {
        var usecaseRequest = buildDeleteProductUsecaseRequest(request);
        var usecaseResponse = deleteProductUsecase.execute(usecaseRequest);
        return buildDeleteProductProcessorResponse(usecaseResponse);
    }

    private DeleteProductUsecaseRequest buildDeleteProductUsecaseRequest(DeleteProductProcessorRequest request) {
        return DeleteProductUsecaseRequest.builder()
                .productId(request.getProductId())
                .build();
    }

    private DeleteProductProcessorResponse buildDeleteProductProcessorResponse(DeleteProductUsecaseResponse usecaseResponse) {
        return DeleteProductProcessorResponse.builder()
                .productId(usecaseResponse.getProductId())
                .deletedAt(usecaseResponse.getDeletedAt())
                .build();
    }
}
