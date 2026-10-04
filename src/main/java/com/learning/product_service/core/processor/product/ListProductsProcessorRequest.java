package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.ProcessorRequest;
import com.learning.product_service.core.domain.ProductFilter;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListProductsProcessorRequest implements ProcessorRequest {
    private ProductFilter filter;
    private String categorySlug;
    private Pageable pageable;
}
