package com.lucasnunesg.banksystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class InvalidAccountTypeException extends BankSystemGenericException{

    private final String details;

    public InvalidAccountTypeException(String details) {
        this.details = details;
    }

    @Override
    public ProblemDetail createProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Invalid account type");
        problemDetail.setDetail(details);

        return problemDetail;
    }
}
