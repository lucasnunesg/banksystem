package com.lucasnunesg.banksystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ResourceNotFoundException extends BankSystemGenericException{

    private final String details;

    public ResourceNotFoundException(String details) {
        this.details = details;
    }

    @Override
    public ProblemDetail createProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Resource not found exception");
        problemDetail.setDetail(details);

        return problemDetail;
    }
}
