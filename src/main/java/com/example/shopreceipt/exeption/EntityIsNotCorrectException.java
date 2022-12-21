package com.example.shopreceipt.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
public class EntityIsNotCorrectException extends RuntimeException {

    public EntityIsNotCorrectException() {
        this("Entity is not correct");
    }

    public EntityIsNotCorrectException(String message) {
        super(message);
    }
}