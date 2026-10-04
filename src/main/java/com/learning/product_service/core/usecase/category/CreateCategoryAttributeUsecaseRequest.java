package com.learning.product_service.core.usecase.category;

import com.learning.product_service.core.usecase.UsecaseRequest;
import com.learning.product_service.core.domain.AttributeDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryAttributeUsecaseRequest implements UsecaseRequest {
    private Long categoryId;
    private String attrKey;
    private AttributeDataType dataType;
    private Boolean required;
    private Boolean facet;
    private List<String> enumValues;
}
