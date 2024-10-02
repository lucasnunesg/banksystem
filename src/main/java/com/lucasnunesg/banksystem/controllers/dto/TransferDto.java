package com.lucasnunesg.banksystem.controllers.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferDto (
        @DecimalMin("0.01") @NotNull BigDecimal value,
        @NotBlank Long payer,
        @NotBlank Long payee) { }
