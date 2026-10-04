package com.learning.product_service.gateway.repository;

import com.learning.product_service.gateway.entity.CategoryAttributeEntity;
import com.learning.product_service.gateway.entity.CategoryAttributeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryAttributeRepository extends JpaRepository<CategoryAttributeEntity, CategoryAttributeId> {

    List<CategoryAttributeEntity> findAllByCategoryIdIn(Collection<Long> categoryIds);
}
