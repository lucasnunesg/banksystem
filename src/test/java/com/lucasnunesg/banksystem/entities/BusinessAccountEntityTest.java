package com.lucasnunesg.banksystem.entities;

import com.lucasnunesg.banksystem.entities.enums.AccountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessAccountEntityTest {

    @Test
    void testBusinessAccountConstructor() {
        String fullName = "Dunder Mifflin";
        String document = "123456789";
        String email = "michael_scott@dundermifflin.com";
        String password = "management";

        BusinessAccount businessAccount = new BusinessAccount(fullName, document, email, password);

        assertEquals(fullName, businessAccount.getFullName());
        assertEquals(document, businessAccount.getDocument());
        assertEquals(email, businessAccount.getEmail());
        assertEquals(password, businessAccount.getPassword());
        assertEquals(AccountType.BUSINESS, businessAccount.getAccountType());
    }
}