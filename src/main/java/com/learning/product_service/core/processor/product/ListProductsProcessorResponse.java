package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.ProcessorResponse;
import com.learning.product_service.core.domain.ProductPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListProductsProcessorResponse implements ProcessorResponse {
    private ProductPage page;
}
