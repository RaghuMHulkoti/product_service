package com.learning.product_service.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    private Long id;
    private String slug;
    private String name;
    private Boolean active;
}
