package com.example.demo.services;

public interface Exceptions
{
    String EXISTING_USER_EXCEPTION = "Product with this name already in the database";
    String NOT_FOUND_USER = "Could not find product with id = ";
    String NOT_ENOUGH_QUANTITY = "Quantity of the product is not enough.";
    String SERVICE_ERROR_EXCEPTION = "Order service is unreachable";
}
