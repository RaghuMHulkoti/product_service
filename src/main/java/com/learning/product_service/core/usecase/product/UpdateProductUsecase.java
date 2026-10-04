package com.learning.product_service.core.usecase.product;

import com.learning.product_service.common.EventType;
import com.learning.product_service.core.domain.Product;
import com.learning.product_service.core.domain.ProductEventData;
import com.learning.product_service.core.service.BrandService;
import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.service.OutboxService;
import com.learning.product_service.core.service.ProductService;
import com.learning.product_service.core.usecase.Usecase;
import com.learning.product_service.exception.OptimisticLockingException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Partial update (PATCH): a null field in the request means "leave unchanged".
 */
@RequiredArgsConstructor
public class UpdateProductUsecase implements Usecase<UpdateProductUsecaseRequest, UpdateProductUsecaseResponse> {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public UpdateProductUsecaseResponse execute(UpdateProductUsecaseRequest request) {
        var current = productService.getRequired(request.getProductId());
        if (request.getExpectedVersion() != null && !Objects.equals(request.getExpectedVersion(), current.getVersion())) {
            throw new OptimisticLockingException("Product was changed by someone else (expected version "
                    + request.getExpectedVersion() + ", current " + current.getVersion() + "). Reload and retry.");
        }

        var changed = applyChanges(current, request);

        var category = Objects.equals(changed.getCategoryId(), current.getCategoryId())
                ? categoryService.getRequired(changed.getCategoryId())
                : categoryService.getRequiredActive(changed.getCategoryId());
        if (request.getBrandId() != null) {
            brandService.getRequired(request.getBrandId());
        }
        if (request.getGtin() != null && !request.getGtin().equals(current.getGtin())) {
            productService.ensureGtinIsFree(request.getGtin(), current.getId());
        }
        categoryService.validateAttributes(category, changed.getAttributes());

        var saved = productService.update(changed);
        outboxService.append(saved.getId(), EventType.PRODUCT_UPDATED, ProductEventData.of(saved, category));

        return UpdateProductUsecaseResponse.builder()
                .product(saved)
                .build();
    }

    private Product applyChanges(Product current, UpdateProductUsecaseRequest request) {
        return current.toBuilder()
                .gtin(valueOr(request.getGtin(), current.getGtin()))
                .title(valueOr(request.getTitle(), current.getTitle()))
                .description(valueOr(request.getDescription(), current.getDescription()))
                .brandId(valueOr(request.getBrandId(), current.getBrandId()))
                .categoryId(valueOr(request.getCategoryId(), current.getCategoryId()))
                .attributes(valueOr(request.getAttributes(), current.getAttributes()))
                .mediaRoot(valueOr(request.getMediaRoot(), current.getMediaRoot()))
                .status(valueOr(request.getStatus(), current.getStatus()))
                .weightGrams(valueOr(request.getWeightGrams(), current.getWeightGrams()))
                .dimLengthMm(valueOr(request.getDimLengthMm(), current.getDimLengthMm()))
                .dimWidthMm(valueOr(request.getDimWidthMm(), current.getDimWidthMm()))
                .dimHeightMm(valueOr(request.getDimHeightMm(), current.getDimHeightMm()))
                .hazardous(valueOr(request.getHazardous(), current.getHazardous()))
                .build();
    }

    private static <T> T valueOr(T requested, T current) {
        return requested != null ? requested : current;
    }
}
