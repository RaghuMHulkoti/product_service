package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.ProcessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProductProcessorResponse implements ProcessorResponse {
    private UUID productId;
    private OffsetDateTime deletedAt;
}
