package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.CategoryAttribute;
import com.learning.product_service.gateway.repository.CategoryAttributeRepository;

import java.util.Collection;
import java.util.List;

public class CategoryAttributeGatewayImpl implements CategoryAttributeGateway {

    private final CategoryAttributeRepository repository;
    private final ProductEntityMapper mapper;

    public CategoryAttributeGatewayImpl(CategoryAttributeRepository repository, ProductEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CategoryAttribute save(CategoryAttribute attribute) {
        return mapper.toDomain(repository.save(mapper.toEntity(attribute)));
    }

    @Override
    public List<CategoryAttribute> findByCategoryIds(Collection<Long> categoryIds) {
        return repository.findAllByCategoryIdIn(categoryIds).stream().map(mapper::toDomain).toList();
    }
}
