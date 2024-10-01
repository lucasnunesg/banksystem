package com.lucasnunesg.banksystem.controllers;

import com.lucasnunesg.banksystem.exceptions.BankSystemGenericException;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BankSystemGenericException.class)
    public ProblemDetail handleBankSystemGenericException (BankSystemGenericException e) {
        return e.createProblemDetail();
    }
}
