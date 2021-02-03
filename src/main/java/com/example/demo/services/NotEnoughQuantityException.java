package com.example.demo.services;

public class NotEnoughQuantityException extends Throwable
{
    public NotEnoughQuantityException(String notEnoughQuantity)
    {
        super(notEnoughQuantity);
    }
}
