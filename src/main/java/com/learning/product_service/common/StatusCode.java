package com.learning.product_service.common;

public enum StatusCode {
    SUCCESS,
    FAILURE;

    public static boolean isSuccess(StatusCode statusCode) {
        return SUCCESS.equals(statusCode);
    }
}
