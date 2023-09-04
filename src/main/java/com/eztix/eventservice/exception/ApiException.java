package com.eztix.eventservice.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

public class ApiException {

    private  String path;
    private HttpStatus status;
    private String message;
    private ZonedDateTime timestamp;

    public ApiException(String path, HttpStatus status, String message, ZonedDateTime timestamp) {
        this.path = path;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
