package com.example.demo.services;

public class ProductNotFoundException extends Throwable
{
    public ProductNotFoundException(long id)
    {
        super(Exceptions.NOT_FOUND_USER + id);
    }
}
