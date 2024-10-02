package com.lucasnunesg.banksystem.entities;

import com.lucasnunesg.banksystem.entities.enums.AccountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


class AccountEntityTest {

    @Test
    void testAccountEntityConstructorAndGetters() {
        String fullName = "John Doe";
        String document = "123456789";
        String email = "john.doe@example.com";
        String password = "password123";
        AccountType accountType = AccountType.PERSONAL;


        Account account = new Account(fullName, document, email, password, accountType) {};

        assertEquals(fullName, account.getFullName());
        assertEquals(document, account.getDocument());
        assertEquals(email, account.getEmail());
        assertEquals(password, account.getPassword());
        assertEquals(accountType, account.getAccountType());
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

}