package com.learning.product_service.core.usecase.product;

import com.learning.product_service.common.EventType;
import com.learning.product_service.core.domain.ProductStatus;
import com.learning.product_service.core.service.OutboxService;
import com.learning.product_service.core.service.ProductService;
import com.learning.product_service.core.usecase.Usecase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Soft delete: the row stays (orders and offers still reference it), but it disappears from every read.
 */
@RequiredArgsConstructor
public class DeleteProductUsecase implements Usecase<DeleteProductUsecaseRequest, DeleteProductUsecaseResponse> {

    private final ProductService productService;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public DeleteProductUsecaseResponse execute(DeleteProductUsecaseRequest request) {
        var product = productService.getRequired(request.getProductId());
        var deletedAt = OffsetDateTime.now(ZoneOffset.UTC);

        var deleted = productService.update(product.toBuilder()
                .status(ProductStatus.DELISTED)
                .deletedAt(deletedAt)
                .build());
        outboxService.append(deleted.getId(), EventType.PRODUCT_DELETED,
                new ProductDeletedData(deleted.getId(), deleted.getSku(), deletedAt));

        return DeleteProductUsecaseResponse.builder()
                .productId(deleted.getId())
                .deletedAt(deletedAt)
                .build();
    }

    private record ProductDeletedData(UUID productId, String sku, OffsetDateTime deletedAt) {
    }
}
