package com.learning.product_service.core.processor.media;

import com.learning.product_service.core.processor.ProcessorResponse;
import com.learning.product_service.core.domain.ProductMedia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductMediaProcessorResponse implements ProcessorResponse {
    private ProductMedia media;
}
