package com.learning.product_service.core.usecase.category;

import com.learning.product_service.core.usecase.UsecaseResponse;
import com.learning.product_service.core.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCategoryTreeUsecaseResponse implements UsecaseResponse {
    private List<Category> roots;
}
