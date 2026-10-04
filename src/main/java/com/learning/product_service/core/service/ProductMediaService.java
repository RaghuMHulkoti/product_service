package com.learning.product_service.core.service;

import com.learning.product_service.core.domain.ProductMedia;
import com.learning.product_service.gateway.ProductMediaGateway;

import java.util.List;
import java.util.UUID;

public class ProductMediaService {

    private final ProductMediaGateway productMediaGateway;

    public ProductMediaService(ProductMediaGateway productMediaGateway) {
        this.productMediaGateway = productMediaGateway;
    }

    public ProductMedia add(ProductMedia media) {
        return productMediaGateway.create(media);
    }

    public List<ProductMedia> list(UUID productId) {
        return productMediaGateway.findByProductId(productId);
    }
}
