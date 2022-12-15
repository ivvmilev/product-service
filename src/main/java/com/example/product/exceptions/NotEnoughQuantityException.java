package com.example.product.exceptions;

public class NotEnoughQuantityException extends RuntimeException {
    public NotEnoughQuantityException(String notEnoughQuantity) {
        super(notEnoughQuantity);
    }
}
