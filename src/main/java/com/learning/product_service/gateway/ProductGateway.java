package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.Product;
import com.learning.product_service.core.domain.ProductFilter;
import com.learning.product_service.core.domain.ProductPage;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Gateway for product persistence. Core layers depend on this interface, never on JPA directly.
 */
public interface ProductGateway {

    Product create(Product product);

    /** Saves changes; throws OptimisticLockingException when the product's version is stale. */
    Product update(Product product);

    /** Finds a product that is not soft-deleted. */
    Optional<Product> findById(UUID productId);

    boolean existsActiveSku(String sku);

    /** True when another non-deleted product already uses {@code gtin}. {@code excludeId} may be null. */
    boolean existsActiveGtin(String gtin, UUID excludeId);

    ProductPage search(ProductFilter filter, Pageable pageable);
}
