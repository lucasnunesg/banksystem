package com.lucasnunesg.banksystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class UnauthorizedTransactionException extends BankSystemGenericException{

    private final String details;

    public UnauthorizedTransactionException(String details) {
        this.details = details;
    }

    @Override
    public ProblemDetail createProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problemDetail.setTitle("Authorization Failed");
        problemDetail.setDetail(details);

        return problemDetail;
    }
}
