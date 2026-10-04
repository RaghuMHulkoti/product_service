package com.learning.product_service.core.service;

import com.learning.product_service.core.domain.Brand;
import com.learning.product_service.exception.BrandNotFoundException;
import com.learning.product_service.exception.DuplicateResourceException;
import com.learning.product_service.gateway.BrandGateway;

import java.util.List;

public class BrandService {

    private final BrandGateway brandGateway;

    public BrandService(BrandGateway brandGateway) {
        this.brandGateway = brandGateway;
    }

    public Brand create(String slug, String name) {
        if (brandGateway.existsBySlug(slug)) {
            throw new DuplicateResourceException("Brand slug already exists: " + slug);
        }
        return brandGateway.create(Brand.builder().slug(slug).name(name).active(true).build());
    }

    public Brand getRequired(Long brandId) {
        return brandGateway.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
    }

    /** Null-safe lookup used when rendering a product whose brand is optional. */
    public Brand findOrNull(Long brandId) {
        return brandId == null ? null : brandGateway.findById(brandId).orElse(null);
    }

    public List<Brand> list() {
        return brandGateway.findAll();
    }
}
