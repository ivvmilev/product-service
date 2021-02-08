package com.example.demo.exceptions;

import com.example.demo.exceptions.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Error> handle(ProductNotFoundException e) {
        Error error = new Error();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
