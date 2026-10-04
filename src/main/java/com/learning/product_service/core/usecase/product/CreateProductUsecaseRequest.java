package com.learning.product_service.core.usecase.product;

import com.learning.product_service.core.usecase.UsecaseRequest;
import com.learning.product_service.core.domain.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductUsecaseRequest implements UsecaseRequest {
    private String sku;
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
