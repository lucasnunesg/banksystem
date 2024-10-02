package com.lucasnunesg.banksystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ExternalServiceUnavailableException extends BankSystemGenericException{

    private final String details;

    public ExternalServiceUnavailableException(String details) {
        this.details = details;
    }

    @Override
    public ProblemDetail createProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);

        problemDetail.setTitle("External Authorization API Unavailable");
        problemDetail.setDetail(details);

        return problemDetail;
    }
}
