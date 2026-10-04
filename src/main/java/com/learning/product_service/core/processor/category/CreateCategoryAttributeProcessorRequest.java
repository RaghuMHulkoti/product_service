package com.learning.product_service.core.processor.category;

import com.learning.product_service.core.processor.ProcessorRequest;
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
public class CreateCategoryAttributeProcessorRequest implements ProcessorRequest {
    private Long categoryId;
    private String attrKey;
    private AttributeDataType dataType;
    private Boolean required;
    private Boolean facet;
    private List<String> enumValues;
}
