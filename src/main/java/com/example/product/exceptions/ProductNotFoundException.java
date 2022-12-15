package com.example.product.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(long id) {
        super(Exceptions.NOT_FOUND_USER + id);
    }
}
