package com.learning.product_service.gateway;

import com.learning.product_service.core.domain.Product;
import com.learning.product_service.core.domain.ProductFilter;
import com.learning.product_service.core.domain.ProductPage;
import com.learning.product_service.exception.OptimisticLockingException;
import com.learning.product_service.gateway.entity.ProductEntity;
import com.learning.product_service.gateway.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.Optional;

public class ProductGatewayImpl implements ProductGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductGatewayImpl.class);

    private final ProductRepository repository;
    private final ProductEntityMapper mapper;

    public ProductGatewayImpl(ProductRepository repository, ProductEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Product create(Product product) {
        log.debug("Creating product sku={}", product.getSku());
        return mapper.toDomain(repository.save(mapper.toEntity(product)));
    }

    @Override
    public Product update(Product product) {
        try {
            // saveAndFlush so a version conflict surfaces here, not at commit time
            return mapper.toDomain(repository.saveAndFlush(mapper.toEntity(product)));
        } catch (ObjectOptimisticLockingFailureException e) {
            log.warn("Optimistic locking failure for product: {}", product.getId());
            throw new OptimisticLockingException("Product version conflict. Reload and retry.", e);
        }
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        return repository.findByIdAndDeletedAtIsNull(productId).map(mapper::toDomain);
    }

    @Override
    public boolean existsActiveSku(String sku) {
        return repository.existsBySkuAndDeletedAtIsNull(sku);
    }

    @Override
    public boolean existsActiveGtin(String gtin, UUID excludeId) {
        return excludeId == null
                ? repository.existsByGtinAndDeletedAtIsNull(gtin)
                : repository.existsByGtinAndDeletedAtIsNullAndIdNot(gtin, excludeId);
    }

    @Override
    public ProductPage search(ProductFilter filter, Pageable pageable) {
        var page = repository.findAll(toSpecification(filter), pageable);
        return new ProductPage(
                page.getContent().stream().map(mapper::toDomain).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private static Specification<ProductEntity> toSpecification(ProductFilter filter) {
        return (root, query, cb) -> {
            var predicates = new ArrayList<Predicate>();
            predicates.add(cb.isNull(root.get("deletedAt")));

            if (filter.getIds() != null && !filter.getIds().isEmpty()) {
                predicates.add(root.get("id").in(filter.getIds()));
            }
            if (filter.getCategoryIds() != null) {
                // An empty list means "no matching category", so nothing can match
                predicates.add(filter.getCategoryIds().isEmpty()
                        ? cb.disjunction()
                        : root.get("categoryId").in(filter.getCategoryIds()));
            }
            if (filter.getBrandId() != null) {
                predicates.add(cb.equal(root.get("brandId"), filter.getBrandId()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            if (filter.getQuery() != null && !filter.getQuery().isBlank()) {
                var pattern = "%" + filter.getQuery().trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.like(cb.lower(root.get("title")), pattern));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
