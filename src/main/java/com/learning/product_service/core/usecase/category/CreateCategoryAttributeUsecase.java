package com.learning.product_service.core.usecase.category;

import com.learning.product_service.core.domain.AttributeDataType;
import com.learning.product_service.core.domain.CategoryAttribute;
import com.learning.product_service.core.service.CategoryService;
import com.learning.product_service.core.usecase.Usecase;
import com.learning.product_service.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CreateCategoryAttributeUsecase
        implements Usecase<CreateCategoryAttributeUsecaseRequest, CreateCategoryAttributeUsecaseResponse> {

    private final CategoryService categoryService;

    @Override
    @Transactional
    public CreateCategoryAttributeUsecaseResponse execute(CreateCategoryAttributeUsecaseRequest request) {
        var isEnum = request.getDataType() == AttributeDataType.ENUM;
        var hasValues = request.getEnumValues() != null && !request.getEnumValues().isEmpty();
        if (isEnum != hasValues) {
            throw new BusinessRuleViolationException("enumValues must be given for ENUM attributes, and only for them");
        }

        var attribute = categoryService.addAttribute(CategoryAttribute.builder()
                .categoryId(request.getCategoryId())
                .attrKey(request.getAttrKey())
                .dataType(request.getDataType())
                .required(Boolean.TRUE.equals(request.getRequired()))
                .facet(Boolean.TRUE.equals(request.getFacet()))
                .enumValues(request.getEnumValues())
                .build());

        return CreateCategoryAttributeUsecaseResponse.builder()
                .attribute(attribute)
                .build();
    }
}
