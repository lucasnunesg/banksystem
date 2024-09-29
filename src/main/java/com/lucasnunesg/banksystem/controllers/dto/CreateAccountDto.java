package com.lucasnunesg.banksystem.controllers.dto;

import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.BusinessAccount;
import com.lucasnunesg.banksystem.entities.PersonalAccount;
import com.lucasnunesg.banksystem.entities.enums.AccountType;
import jakarta.validation.constraints.NotBlank;


public record CreateAccountDto(
        @NotBlank String fullName,
        @NotBlank String document,
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank AccountType accountType){


    public Account toEntity() {
        if (accountType == AccountType.BUSINESS) {
            return new BusinessAccount(fullName, document, email, password);
        } else if (accountType == AccountType.PERSONAL) {
            return new PersonalAccount(fullName, document, email, password);
        }
        throw new IllegalArgumentException("Tipo de conta inválido: " + accountType);
    }
}
