package com.learning.product_service.gateway.repository;

import com.learning.product_service.gateway.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {

    boolean existsBySlug(String slug);

    List<BrandEntity> findAllByOrderByNameAsc();
}
