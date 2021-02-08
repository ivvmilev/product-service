package com.example.demo.exceptions;

public class ProductNotFoundException extends Throwable
{
    public ProductNotFoundException(long id)
    {
        super(Exceptions.NOT_FOUND_USER + id);
    }
}
