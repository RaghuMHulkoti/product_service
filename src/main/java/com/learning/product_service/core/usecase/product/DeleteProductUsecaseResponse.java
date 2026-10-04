package com.learning.product_service.core.usecase.product;

import com.learning.product_service.core.usecase.UsecaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProductUsecaseResponse implements UsecaseResponse {
    private UUID productId;
    private OffsetDateTime deletedAt;
}
