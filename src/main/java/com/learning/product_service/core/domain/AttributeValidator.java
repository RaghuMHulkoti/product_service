package com.learning.product_service.core.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Checks product attributes against the attribute schema of the product's category (and its ancestors).
 * Pure domain logic: no Spring, no IO.
 */
public final class AttributeValidator {

    private AttributeValidator() {
    }

    /** Returns one message per violation; an empty list means the attributes are valid. */
    public static List<String> validate(Map<String, Object> attributes, List<CategoryAttribute> schema) {
        var values = attributes == null ? Map.<String, Object>of() : attributes;
        var violations = new ArrayList<String>();

        for (var definition : schema) {
            var value = values.get(definition.getAttrKey());
            if (value == null) {
                if (Boolean.TRUE.equals(definition.getRequired())) {
                    violations.add("Missing required attribute '" + definition.getAttrKey() + "'");
                }
                continue;
            }
            if (!matchesType(value, definition)) {
                violations.add("Attribute '" + definition.getAttrKey() + "' must be " + describe(definition));
            }
        }
        return violations;
    }

    private static boolean matchesType(Object value, CategoryAttribute definition) {
        return switch (definition.getDataType()) {
            case STRING -> value instanceof String;
            case NUMBER -> value instanceof Number;
            case BOOL -> value instanceof Boolean;
            case ENUM -> value instanceof String s
                    && definition.getEnumValues() != null
                    && definition.getEnumValues().contains(s);
        };
    }

    private static String describe(CategoryAttribute definition) {
        return switch (definition.getDataType()) {
            case STRING -> "a string";
            case NUMBER -> "a number";
            case BOOL -> "true or false";
            case ENUM -> "one of " + definition.getEnumValues();
        };
    }
}
