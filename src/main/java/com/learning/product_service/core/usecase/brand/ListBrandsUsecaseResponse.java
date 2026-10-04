package com.learning.product_service.core.usecase.brand;

import com.learning.product_service.core.usecase.UsecaseResponse;
import com.learning.product_service.core.domain.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListBrandsUsecaseResponse implements UsecaseResponse {
    private List<Brand> brands;
}
