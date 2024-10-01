package com.lucasnunesg.banksystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class AccountAlreadyExistsException extends BankSystemGenericException{

    private final String details;

    public AccountAlreadyExistsException(String details) {
        this.details = details;
    }

    @Override
    public ProblemDetail problemDetailFactory() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problemDetail.setTitle("Account with given e-mail and/or Document already exists");
        problemDetail.setDetail(details);

        return problemDetail;
    }
}
