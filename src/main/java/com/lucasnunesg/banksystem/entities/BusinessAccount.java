package com.lucasnunesg.banksystem.entities;

import com.lucasnunesg.banksystem.entities.enums.AccountType;
import jakarta.persistence.Entity;

@Entity
public class BusinessAccount extends Account{

    public BusinessAccount(String fullName, String document, String email, String password) {
        super(fullName, document, email, password, AccountType.BUSINESS);
    }

    public BusinessAccount() {
        super();
    }
}
