package com.learning.product_service.gateway.repository;

import com.learning.product_service.gateway.entity.ProductMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductMediaRepository extends JpaRepository<ProductMediaEntity, Long> {

    List<ProductMediaEntity> findAllByProductIdOrderBySortOrderAscIdAsc(UUID productId);
}
