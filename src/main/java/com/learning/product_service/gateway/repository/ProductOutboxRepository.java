package com.learning.product_service.gateway.repository;

import com.learning.product_service.gateway.entity.ProductOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductOutboxRepository extends JpaRepository<ProductOutboxEntity, Long> {

    List<ProductOutboxEntity> findAllByAggregateIdOrderByIdAsc(UUID aggregateId);
}
