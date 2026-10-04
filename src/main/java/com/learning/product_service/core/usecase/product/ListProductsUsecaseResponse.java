package com.learning.product_service.core.usecase.product;

import com.learning.product_service.core.usecase.UsecaseResponse;
import com.learning.product_service.core.domain.ProductPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListProductsUsecaseResponse implements UsecaseResponse {
    private ProductPage page;
}
