package com.learning.product_service.core.usecase.media;

import com.learning.product_service.common.EventType;
import com.learning.product_service.core.domain.ProductEventData;
import com.learning.product_service.core.domain.ProductMedia;
import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.service.OutboxService;
import com.learning.product_service.core.service.ProductMediaService;
import com.learning.product_service.core.service.ProductService;
import com.learning.product_service.core.usecase.Usecase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

/**
 * Registers a media file that the client already uploaded (e.g. via a pre-signed S3 URL).
 * Emits PRODUCT_UPDATED so the Search index picks up the new image.
 */
@RequiredArgsConstructor
public class AddProductMediaUsecase implements Usecase<AddProductMediaUsecaseRequest, AddProductMediaUsecaseResponse> {

    private final ProductService productService;
    private final ProductMediaService productMediaService;
    private final CategoryService categoryService;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public AddProductMediaUsecaseResponse execute(AddProductMediaUsecaseRequest request) {
        var product = productService.getRequired(request.getProductId());

        var media = productMediaService.add(ProductMedia.builder()
                .productId(product.getId())
                .kind(request.getKind())
                .url(request.getUrl())
                .sortOrder(request.getSortOrder() != null ? request.getSortOrder() : 0)
                .altText(request.getAltText())
                .build());
        outboxService.append(product.getId(), EventType.PRODUCT_UPDATED,
                ProductEventData.of(product, categoryService.getRequired(product.getCategoryId())));

        return AddProductMediaUsecaseResponse.builder()
                .media(media)
                .build();
    }
}
