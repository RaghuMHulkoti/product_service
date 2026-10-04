package com.learning.product_service.core.processor.product;

import com.learning.product_service.core.processor.ProcessorRequest;
import com.learning.product_service.core.domain.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductProcessorRequest implements ProcessorRequest {
    private UUID productId;
    private Long expectedVersion;
    private String gtin;
    private String title;
    private String description;
    private Long brandId;
    private Long categoryId;
    private Map<String, Object> attributes;
    private String mediaRoot;
    private ProductStatus status;
    private Integer weightGrams;
    private Integer dimLengthMm;
    private Integer dimWidthMm;
    private Integer dimHeightMm;
    private Boolean hazardous;
}
