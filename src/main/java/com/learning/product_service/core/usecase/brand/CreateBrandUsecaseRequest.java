package com.learning.product_service.core.usecase.brand;

import com.learning.product_service.core.usecase.UsecaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandUsecaseRequest implements UsecaseRequest {
    private String slug;
    private String name;
}
