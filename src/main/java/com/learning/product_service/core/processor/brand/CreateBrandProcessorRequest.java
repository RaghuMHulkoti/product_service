package com.learning.product_service.core.processor.brand;

import com.learning.product_service.core.processor.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandProcessorRequest implements ProcessorRequest {
    private String slug;
    private String name;
}
