package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.ProductMedia;
import com.learning.product_service.gateway.repository.ProductMediaRepository;

import java.util.List;
import java.util.UUID;

public class ProductMediaGatewayImpl implements ProductMediaGateway {

    private final ProductMediaRepository repository;
    private final ProductEntityMapper mapper;

    public ProductMediaGatewayImpl(ProductMediaRepository repository, ProductEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ProductMedia create(ProductMedia media) {
        return mapper.toDomain(repository.save(mapper.toEntity(media)));
    }

    @Override
    public List<ProductMedia> findByProductId(UUID productId) {
        return repository.findAllByProductIdOrderBySortOrderAscIdAsc(productId).stream().map(mapper::toDomain).toList();
    }
}
