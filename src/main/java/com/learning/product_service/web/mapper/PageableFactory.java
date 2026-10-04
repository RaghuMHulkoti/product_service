package com.learning.product_service.web.mapper;

import com.learning.product_service.exception.InvalidRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Locale;
import java.util.Set;

/**
 * Builds a Pageable from {@code page}, {@code size} and {@code sort=field,asc|desc}.
 * Only whitelisted fields can be sorted on, so a bad field is a 400, not a 500.
 */
public final class PageableFactory {

    private static final Set<String> SORTABLE_FIELDS = Set.of("title", "sku", "createdAt", "updatedAt");

    private PageableFactory() {
    }

    public static Pageable of(int page, int size, String sort) {
        var parts = sort.split(",");
        var field = parts[0].trim();
        if (!SORTABLE_FIELDS.contains(field)) {
            throw new InvalidRequestException("Cannot sort by '" + field + "'. Allowed: " + SORTABLE_FIELDS);
        }
        var direction = parts.length > 1 ? parts[1].trim().toLowerCase(Locale.ROOT) : "asc";
        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new InvalidRequestException("Sort direction must be 'asc' or 'desc'");
        }
        return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), field).and(Sort.by("id")));
    }
}
