package com.learning.product_service.core.usecase.brand;

import com.learning.product_service.core.usecase.UsecaseRequest;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class ListBrandsUsecaseRequest implements UsecaseRequest {
}
