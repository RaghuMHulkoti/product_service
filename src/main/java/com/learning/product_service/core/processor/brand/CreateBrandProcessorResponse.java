package com.learning.product_service.core.processor.brand;

import com.learning.product_service.core.processor.ProcessorResponse;
import com.learning.product_service.core.domain.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandProcessorResponse implements ProcessorResponse {
    private Brand brand;
}
