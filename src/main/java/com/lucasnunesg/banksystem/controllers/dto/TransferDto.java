package com.lucasnunesg.banksystem.controllers.dto;

import com.lucasnunesg.banksystem.entities.Transfer;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferDto (
        @NotBlank Long senderId,
        @NotBlank Long receiverId,
        @DecimalMin("0.01") @NotNull BigDecimal amount) { }
