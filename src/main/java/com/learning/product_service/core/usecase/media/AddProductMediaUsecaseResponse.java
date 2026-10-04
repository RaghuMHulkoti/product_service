package com.learning.product_service.core.usecase.media;

import com.learning.product_service.core.usecase.UsecaseResponse;
import com.learning.product_service.core.domain.ProductMedia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddProductMediaUsecaseResponse implements UsecaseResponse {
    private ProductMedia media;
}
