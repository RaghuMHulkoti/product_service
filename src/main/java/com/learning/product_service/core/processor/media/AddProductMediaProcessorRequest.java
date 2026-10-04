package com.learning.product_service.core.processor.media;

import com.learning.product_service.core.processor.ProcessorRequest;
import com.learning.product_service.core.domain.MediaKind;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductMediaProcessorRequest implements ProcessorRequest {
    private UUID productId;
    private MediaKind kind;
    private String url;
    private Integer sortOrder;
    private String altText;
}
