package com.learning.product_service.core.usecase.brand;

import com.learning.product_service.core.usecase.UsecaseResponse;
import com.learning.product_service.core.domain.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandUsecaseResponse implements UsecaseResponse {
    private Brand brand;
}
