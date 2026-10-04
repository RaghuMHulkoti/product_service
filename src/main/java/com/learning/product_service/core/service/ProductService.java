package com.learning.product_service.core.service;

import com.learning.product_service.core.domain.Product;
import com.learning.product_service.core.domain.ProductFilter;
import com.learning.product_service.core.domain.ProductPage;
import com.learning.product_service.exception.DuplicateResourceException;
import com.learning.product_service.exception.ProductNotFoundException;
import com.learning.product_service.gateway.ProductGateway;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public class ProductService {

    private final ProductGateway productGateway;

    public ProductService(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    /** Returns a non-deleted product, whatever its status. */
    public Product getRequired(UUID productId) {
        return productGateway.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    /** Returns a product a customer may see (ACTIVE and not deleted). Anything else looks like 404. */
    public Product getPubliclyVisible(UUID productId) {
        var product = getRequired(productId);
        if (!product.isPubliclyVisible()) {
            throw new ProductNotFoundException(productId);
        }
        return product;
    }

    public void ensureSkuIsFree(String sku) {
        if (productGateway.existsActiveSku(sku)) {
            throw new DuplicateResourceException("Product SKU already exists: " + sku);
        }
    }

    /** {@code productId} is the product being edited, or null for a new product. */
    public void ensureGtinIsFree(String gtin, UUID productId) {
        if (gtin != null && productGateway.existsActiveGtin(gtin, productId)) {
            throw new DuplicateResourceException("Product GTIN already exists: " + gtin);
        }
    }

    public Product create(Product product) {
        return productGateway.create(product);
    }

    public Product update(Product product) {
        return productGateway.update(product);
    }

    public ProductPage search(ProductFilter filter, Pageable pageable) {
        return productGateway.search(filter, pageable);
    }
}
