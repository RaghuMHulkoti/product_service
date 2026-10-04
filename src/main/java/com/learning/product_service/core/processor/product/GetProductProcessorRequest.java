package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.ProcessorRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProductProcessorRequest implements ProcessorRequest {
    private UUID productId;
    private Boolean publicView;
    private Boolean includeOffers;
}
