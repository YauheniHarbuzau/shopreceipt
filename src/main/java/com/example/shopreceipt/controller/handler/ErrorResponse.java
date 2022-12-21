package com.example.shopreceipt.controller.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorResponse {

    private String message;

    private HttpStatus httpStatus;

    private int code;

    private Date timestamp;
}