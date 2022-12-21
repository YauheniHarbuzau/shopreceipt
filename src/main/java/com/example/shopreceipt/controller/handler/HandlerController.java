package com.example.shopreceipt.controller.handler;

import com.example.shopreceipt.exeption.EntityIsNotCorrectException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Date;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class HandlerController {

    @ExceptionHandler(EntityIsNotCorrectException.class)
    @ResponseStatus(I_AM_A_TEAPOT)
    public ErrorResponse handleEntityIsNotCorrect(EntityIsNotCorrectException entityIsNotCorrectException) {
        return ErrorResponse.builder().
                message(entityIsNotCorrectException.getMessage()).
                httpStatus(I_AM_A_TEAPOT).
                code(I_AM_A_TEAPOT.value()).
                timestamp(Date.from(Instant.now())).
                build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return ErrorResponse.builder().
                message(entityNotFoundException.getMessage()).
                httpStatus(NOT_FOUND).
                code(NOT_FOUND.value()).
                timestamp(Date.from(Instant.now())).
                build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ErrorResponse.builder().
                message(illegalArgumentException.getMessage()).
                httpStatus(BAD_REQUEST).
                code(BAD_REQUEST.value()).
                timestamp(Date.from(Instant.now())).
                build();
    }
}