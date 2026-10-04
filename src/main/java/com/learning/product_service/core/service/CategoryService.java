package com.learning.product_service.core.service;

import com.learning.product_service.core.domain.AttributeValidator;
import com.learning.product_service.core.domain.Category;
import com.learning.product_service.core.domain.CategoryAttribute;
import com.learning.product_service.exception.BusinessRuleViolationException;
import com.learning.product_service.exception.CategoryNotFoundException;
import com.learning.product_service.exception.DuplicateResourceException;
import com.learning.product_service.gateway.CategoryAttributeGateway;
import com.learning.product_service.gateway.CategoryGateway;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {

    private final CategoryGateway categoryGateway;
    private final CategoryAttributeGateway categoryAttributeGateway;

    public CategoryService(CategoryGateway categoryGateway, CategoryAttributeGateway categoryAttributeGateway) {
        this.categoryGateway = categoryGateway;
        this.categoryAttributeGateway = categoryAttributeGateway;
    }

    public Category create(Long parentId, String slug, String name) {
        if (categoryGateway.existsBySlug(slug)) {
            throw new DuplicateResourceException("Category slug already exists: " + slug);
        }
        var parentPath = parentId == null ? "" : getRequired(parentId).getPath();
        return categoryGateway.create(Category.builder()
                .parentId(parentId)
                .slug(slug)
                .name(name)
                .path(parentPath + "/" + slug)
                .active(true)
                .build());
    }

    public Category getRequired(Long categoryId) {
        return categoryGateway.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(String.valueOf(categoryId)));
    }

    /** Products can only be placed in an active category. */
    public Category getRequiredActive(Long categoryId) {
        var category = getRequired(categoryId);
        if (!Boolean.TRUE.equals(category.getActive())) {
            throw new BusinessRuleViolationException("Category is not active: " + categoryId);
        }
        return category;
    }

    public Category getRequiredBySlug(String slug) {
        return categoryGateway.findBySlug(slug)
                .orElseThrow(() -> new CategoryNotFoundException(slug));
    }

    /** Ids of the category and all categories below it (browse includes sub-categories). */
    public List<Long> subtreeIds(Category category) {
        return categoryGateway.findSubtree(category).stream()
                .filter(c -> Boolean.TRUE.equals(c.getActive()))
                .map(Category::getId)
                .toList();
    }

    /** Active categories as a forest of root nodes, children nested. */
    public List<Category> tree() {
        Map<Long, Category> byId = new LinkedHashMap<>();
        categoryGateway.findAllActive().forEach(c -> byId.put(c.getId(), c.toBuilder().children(new ArrayList<>()).build()));

        var roots = new ArrayList<Category>();
        for (var category : byId.values()) {
            var parent = category.getParentId() == null ? null : byId.get(category.getParentId());
            if (parent != null) {
                parent.getChildren().add(category);
            } else if (category.getParentId() == null) {
                roots.add(category);
            }
            // a child of an inactive parent is hidden together with its parent
        }
        return roots;
    }

    public CategoryAttribute addAttribute(CategoryAttribute attribute) {
        getRequired(attribute.getCategoryId());
        return categoryAttributeGateway.save(attribute);
    }

    /**
     * Checks attributes against the schema of the category and all its ancestors,
     * so "smartphones" inherits what "electronics" requires.
     */
    public void validateAttributes(Category category, Map<String, Object> attributes) {
        var schema = categoryAttributeGateway.findByCategoryIds(lineageIds(category));
        var violations = AttributeValidator.validate(attributes, schema);
        if (!violations.isEmpty()) {
            throw new BusinessRuleViolationException("Product attributes do not match category schema", violations);
        }
    }

    private List<Long> lineageIds(Category category) {
        var ids = new ArrayList<Long>();
        var current = category;
        while (current != null) {
            ids.add(current.getId());
            current = current.getParentId() == null ? null : categoryGateway.findById(current.getParentId()).orElse(null);
        }
        return ids;
    }
}
