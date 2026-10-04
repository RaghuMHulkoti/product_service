package com.learning.product_service.web.response;

import com.learning.product_service.core.domain.AttributeDataType;

import java.util.List;

public record CategoryAttributeResponse(
        Long categoryId,
        String attrKey,
        AttributeDataType dataType,
        Boolean required,
        Boolean facet,
        List<String> enumValues
) {
}
