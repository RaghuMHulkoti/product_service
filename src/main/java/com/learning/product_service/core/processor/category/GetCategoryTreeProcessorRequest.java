package com.learning.product_service.core.processor.category;

import com.learning.product_service.core.processor.ProcessorRequest;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class GetCategoryTreeProcessorRequest implements ProcessorRequest {
}
