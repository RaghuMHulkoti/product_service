package com.learning.product_service.core.usecase.category;

import com.learning.product_service.core.usecase.UsecaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryUsecaseRequest implements UsecaseRequest {
    private Long parentId;
    private String slug;
    private String name;
}
