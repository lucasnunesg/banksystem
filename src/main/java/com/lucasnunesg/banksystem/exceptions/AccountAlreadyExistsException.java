package com.lucasnunesg.banksystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class AccountAlreadyExistsException extends BankSystemGenericException{

    private final String details;

    public AccountAlreadyExistsException(String details) {
        this.details = details;
    }

    @Override
    public ProblemDetail createProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problemDetail.setTitle("Account already exists");
        problemDetail.setDetail(details);

        return problemDetail;
    }
}
