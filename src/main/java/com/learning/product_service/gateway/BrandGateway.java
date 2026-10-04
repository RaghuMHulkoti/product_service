package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandGateway {

    Brand create(Brand brand);

    Optional<Brand> findById(Long brandId);

    boolean existsBySlug(String slug);

    List<Brand> findAll();
}
