package com.learning.product_service.core.processor.category;

import com.learning.product_service.core.processor.ProcessorResponse;
import com.learning.product_service.core.domain.CategoryAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryAttributeProcessorResponse implements ProcessorResponse {
    private CategoryAttribute attribute;
}
