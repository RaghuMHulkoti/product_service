package com.learning.product_service.core.usecase.product;

import com.learning.product_service.core.usecase.UsecaseResponse;
import com.learning.product_service.core.domain.Brand;
import com.learning.product_service.core.domain.Category;
import com.learning.product_service.core.domain.OfferLookup;
import com.learning.product_service.core.domain.Product;
import com.learning.product_service.core.domain.ProductMedia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetProductUsecaseResponse implements UsecaseResponse {
    private Product product;
    private Brand brand;
    private Category category;
    private List<ProductMedia> media;
    private OfferLookup offerLookup;
}
