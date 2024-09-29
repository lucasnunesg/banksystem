package com.lucasnunesg.banksystem.entities;

import com.lucasnunesg.banksystem.entities.enums.AccountType;
import jakarta.persistence.Entity;

@Entity
public class PersonalAccount extends Account{

    public PersonalAccount(String fullName, String document, String email, String password) {
        super(fullName, document, email, password, AccountType.PERSONAL);
    }

    public PersonalAccount() {
        super();
    }
}
