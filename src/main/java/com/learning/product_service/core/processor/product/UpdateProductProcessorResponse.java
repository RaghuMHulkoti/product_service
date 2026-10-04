package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.ProcessorResponse;
import com.learning.product_service.core.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductProcessorResponse implements ProcessorResponse {
    private Product product;
}
