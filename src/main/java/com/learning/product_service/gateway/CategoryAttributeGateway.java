package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.CategoryAttribute;

import java.util.Collection;
import java.util.List;

public interface CategoryAttributeGateway {

    CategoryAttribute save(CategoryAttribute attribute);

    List<CategoryAttribute> findByCategoryIds(Collection<Long> categoryIds);
}
