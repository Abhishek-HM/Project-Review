package com.projectwork.product_service.controller;

public enum ResponseMessages {
    PRODUCT_CREATED_SUCCESS("Product Successfully Created"),
    PRODUCT_NOT_FOUND("No Products Found In Product Service.");

    private final String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
