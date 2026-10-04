package com.learning.product_service.gateway.repository;

import com.learning.product_service.gateway.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);

    List<CategoryEntity> findAllByIsActiveTrueOrderByPathAsc();

    /** The category itself plus every category below it. */
    List<CategoryEntity> findAllByPathOrPathStartingWith(String path, String pathPrefix);
}
