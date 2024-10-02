package com.lucasnunesg.banksystem.controllers.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferDto (
        @NotNull @DecimalMin(value = "0.01", message = "Value must be greater than 0") BigDecimal value,
        @NotNull Long payer,
        @NotNull Long payee) { }
