package com.example.demo.exceptions;

public class NotEnoughQuantityException extends Throwable
{
    public NotEnoughQuantityException(String notEnoughQuantity)
    {
        super(notEnoughQuantity);
    }
}
