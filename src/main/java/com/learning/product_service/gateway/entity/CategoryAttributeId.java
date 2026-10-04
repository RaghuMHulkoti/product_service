package com.learning.product_service.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAttributeId implements Serializable {
    private Long categoryId;
    private String attrKey;
}
