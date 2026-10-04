package com.learning.product_service.core.processor.category;

import com.learning.product_service.core.processor.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryProcessorRequest implements ProcessorRequest {
    private Long parentId;
    private String slug;
    private String name;
}
