package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.ProductMedia;

import java.util.List;
import java.util.UUID;

public interface ProductMediaGateway {

    ProductMedia create(ProductMedia media);

    List<ProductMedia> findByProductId(UUID productId);
}
