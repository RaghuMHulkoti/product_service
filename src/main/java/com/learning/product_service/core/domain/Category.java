package com.learning.product_service.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Category node. {@code path} is the slug chain from the root, e.g. {@code /electronics/mobiles/smartphones}.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Long id;
    private Long parentId;
    private String slug;
    private String name;
    private String path;
    private Boolean active;

    @Builder.Default
    private List<Category> children = new ArrayList<>();

    /** True when {@code other} is this category or sits anywhere below it. */
    public boolean contains(Category other) {
        return other.getPath().equals(path) || other.getPath().startsWith(path + "/");
    }
}
