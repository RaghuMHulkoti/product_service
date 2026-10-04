package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.Brand;
import com.learning.product_service.gateway.repository.BrandRepository;

import java.util.List;
import java.util.Optional;

public class BrandGatewayImpl implements BrandGateway {

    private final BrandRepository repository;
    private final ProductEntityMapper mapper;

    public BrandGatewayImpl(BrandRepository repository, ProductEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Brand create(Brand brand) {
        return mapper.toDomain(repository.save(mapper.toEntity(brand)));
    }

    @Override
    public Optional<Brand> findById(Long brandId) {
        return repository.findById(brandId).map(mapper::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return repository.existsBySlug(slug);
    }

    @Override
    public List<Brand> findAll() {
        return repository.findAllByOrderByNameAsc().stream().map(mapper::toDomain).toList();
    }
}
