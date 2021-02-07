package com.example.demo.services;

public class ServiceErrorException extends Throwable
{
    public ServiceErrorException(String message)
    {
        super(message);
    }
}
