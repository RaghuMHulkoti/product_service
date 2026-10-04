package com.learning.product_service.core.usecase.product;

import com.learning.product_service.core.usecase.UsecaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProductUsecaseRequest implements UsecaseRequest {
    private UUID productId;
}
