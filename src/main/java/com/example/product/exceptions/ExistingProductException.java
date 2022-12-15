package com.example.product.exceptions;

public class ExistingProductException extends RuntimeException {
    public ExistingProductException(String exception) {
        super(exception);
    }
}
