package com.lucasnunesg.banksystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class BankSystemGenericException extends RuntimeException {

    public ProblemDetail createProblemDetail() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Bank System Internal Server Error");

        return problemDetail;
    }
}
