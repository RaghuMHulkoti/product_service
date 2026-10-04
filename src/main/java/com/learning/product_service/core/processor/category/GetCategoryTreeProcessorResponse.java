package com.learning.product_service.core.processor.category;

import com.learning.product_service.core.processor.ProcessorResponse;
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
public class GetCategoryTreeProcessorResponse implements ProcessorResponse {
    private List<Category> roots;
}
