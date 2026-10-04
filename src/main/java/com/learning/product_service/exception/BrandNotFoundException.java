package com.learning.product_service.exception;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(Long brandId) {
        super("Brand not found: " + brandId);
    }
}
