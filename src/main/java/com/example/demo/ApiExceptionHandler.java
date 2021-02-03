package com.example.demo;

import com.example.demo.services.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

//    @SuppressWarnings("rawtypes")
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Error> handle(ConstraintViolationException e) {
//        Error errors = new Er();
//        for (ConstraintViolation violation : e.getConstraintViolations()) {
//            ErrorItem error = new ErrorItem();
//            errors.setCode(violation.getMessageTemplate());
//            error.setMessage(violation.getMessage());
//            errors.addError(error);
//        }
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Error> handle(ProductNotFoundException e) {
        Error error = new Error();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
