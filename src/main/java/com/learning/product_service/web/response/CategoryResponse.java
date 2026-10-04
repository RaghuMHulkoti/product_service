package com.learning.product_service.web.response;

import java.util.List;

public record CategoryResponse(
        Long id,
        Long parentId,
        String slug,
        String name,
        String path,
        Boolean active,
        List<CategoryResponse> children
) {
}
