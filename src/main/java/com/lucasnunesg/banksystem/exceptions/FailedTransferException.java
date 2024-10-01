package com.lucasnunesg.banksystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class FailedTransferException extends BankSystemGenericException{

    private final String details;

    public FailedTransferException(String details) {
        this.details = details;
    }

    @Override
    public ProblemDetail createProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problemDetail.setTitle("There was an issue with the transfer");
        problemDetail.setDetail(details);

        return problemDetail;
    }
}
