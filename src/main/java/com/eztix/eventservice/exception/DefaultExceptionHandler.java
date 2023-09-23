package com.eztix.eventservice.exception;

import com.eztix.eventservice.exception.ApiException;
import com.eztix.eventservice.exception.RequestValidationException;
import com.eztix.eventservice.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiException> handleException(ResourceNotFoundException e,
                                                        HttpServletRequest request) {
        ApiException apiException = new ApiException(
                request.getRequestURI(),
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Singapore"))
        );

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ApiException> handleException(RequestValidationException e,
                                                        HttpServletRequest request) {
        ApiException apiException = new ApiException(
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Singapore"))
        );

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiException> handleException(Exception e,
                                                    HttpServletRequest request) {
        ApiException apiError = new ApiException(
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getMessage(),
                ZonedDateTime.now(ZoneId.of("Singapore"))
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
