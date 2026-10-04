package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);

    Optional<Category> findById(Long categoryId);

    Optional<Category> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<Category> findAllActive();

    /** The category plus every category below it. */
    List<Category> findSubtree(Category category);
}
