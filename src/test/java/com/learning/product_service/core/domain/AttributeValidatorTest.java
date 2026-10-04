package com.learning.product_service.core.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class AttributeValidatorTest {

    private static CategoryAttribute attr(String key, AttributeDataType type, boolean required, List<String> enumValues) {
        return CategoryAttribute.builder()
                .categoryId(1L)
                .attrKey(key)
                .dataType(type)
                .required(required)
                .facet(false)
                .enumValues(enumValues)
                .build();
    }

    private static final List<CategoryAttribute> SCHEMA = List.of(
            attr("color", AttributeDataType.STRING, true, null),
            attr("storage", AttributeDataType.ENUM, true, List.of("128GB", "256GB")),
            attr("weight", AttributeDataType.NUMBER, false, null),
            attr("wireless", AttributeDataType.BOOL, false, null)
    );

    @Test
    void validAttributesPass() {
        var attributes = Map.<String, Object>of("color", "Black", "storage", "256GB", "weight", 187, "wireless", true);

        assertThat(AttributeValidator.validate(attributes, SCHEMA)).isEmpty();
    }

    @Test
    void extraAttributesNotInSchemaAreAllowed() {
        var attributes = Map.<String, Object>of("color", "Black", "storage", "128GB", "marketingName", "Pro");

        assertThat(AttributeValidator.validate(attributes, SCHEMA)).isEmpty();
    }

    @Test
    void missingRequiredAttributesAreReported() {
        assertThat(AttributeValidator.validate(Map.of(), SCHEMA))
                .containsExactly("Missing required attribute 'color'", "Missing required attribute 'storage'");
    }

    @Test
    void nullAttributeMapIsTreatedAsEmpty() {
        assertThat(AttributeValidator.validate(null, SCHEMA)).hasSize(2);
    }

    @Test
    void wrongTypesAreReported() {
        var attributes = Map.<String, Object>of("color", 42, "storage", "512GB", "weight", "heavy", "wireless", "yes");

        assertThat(AttributeValidator.validate(attributes, SCHEMA)).containsExactly(
                "Attribute 'color' must be a string",
                "Attribute 'storage' must be one of [128GB, 256GB]",
                "Attribute 'weight' must be a number",
                "Attribute 'wireless' must be true or false"
        );
    }

    @Test
    void optionalAttributeMayBeAbsent() {
        var attributes = Map.<String, Object>of("color", "Black", "storage", "128GB");

        assertThat(AttributeValidator.validate(attributes, SCHEMA)).isEmpty();
    }
}
