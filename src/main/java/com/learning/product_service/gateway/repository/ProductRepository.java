package com.learning.product_service.gateway.repository;

import com.learning.product_service.gateway.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {

    Optional<ProductEntity> findByIdAndDeletedAtIsNull(UUID id);

    boolean existsBySkuAndDeletedAtIsNull(String sku);

    boolean existsByGtinAndDeletedAtIsNull(String gtin);

    boolean existsByGtinAndDeletedAtIsNullAndIdNot(String gtin, UUID id);
}
