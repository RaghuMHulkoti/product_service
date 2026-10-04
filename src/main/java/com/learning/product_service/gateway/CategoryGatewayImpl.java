package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.Category;
import com.learning.product_service.gateway.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

public class CategoryGatewayImpl implements CategoryGateway {

    private final CategoryRepository repository;
    private final ProductEntityMapper mapper;

    public CategoryGatewayImpl(CategoryRepository repository, ProductEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Category create(Category category) {
        return mapper.toDomain(repository.save(mapper.toEntity(category)));
    }

    @Override
    public Optional<Category> findById(Long categoryId) {
        return repository.findById(categoryId).map(mapper::toDomain);
    }

    @Override
    public Optional<Category> findBySlug(String slug) {
        return repository.findBySlug(slug).map(mapper::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return repository.existsBySlug(slug);
    }

    @Override
    public List<Category> findAllActive() {
        return repository.findAllByIsActiveTrueOrderByPathAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Category> findSubtree(Category category) {
        return repository.findAllByPathOrPathStartingWith(category.getPath(), category.getPath() + "/").stream()
                .map(mapper::toDomain)
                .toList();
    }
}
