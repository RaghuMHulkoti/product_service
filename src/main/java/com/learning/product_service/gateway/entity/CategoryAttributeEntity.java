package com.learning.product_service.gateway.entity;

import com.learning.product_service.core.domain.AttributeDataType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "category_attributes")
@IdClass(CategoryAttributeId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryAttributeEntity {

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Id
    @Column(name = "attr_key", length = 60)
    private String attrKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type", nullable = false, length = 20)
    private AttributeDataType dataType;

    @Column(name = "is_required", nullable = false)
    private Boolean isRequired;

    @Column(name = "is_facet", nullable = false)
    private Boolean isFacet;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "enum_values")
    private List<String> enumValues;
}
