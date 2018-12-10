package com.akurilo.weatherapi.controller;


import exception.NotFoundException;
import exception.NotUniqueException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(WebExchangeBindException ex) {
        BindingResult result = ex.getBindingResult();

        Map<String, String> errors = result.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotUniqueException.class)
    @ResponseBody
    public ResponseEntity<Object> handleNotUniqueException(NotUniqueException e) {
        return new ResponseEntity<>(e.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
    }
}
