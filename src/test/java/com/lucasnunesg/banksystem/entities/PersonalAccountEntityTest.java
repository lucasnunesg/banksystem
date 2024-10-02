package com.lucasnunesg.banksystem.entities;

import com.lucasnunesg.banksystem.entities.enums.AccountType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonalAccountEntityTest {

    @Test
    void testPersonalAccountConstructor() {
        String fullName = "Rick Sanchez";
        String document = "123456789";
        String email = "picklerick@email.com";
        String password = "wubalubadubdub";

        PersonalAccount personalAccount = new PersonalAccount(fullName, document, email, password);

        assertEquals(fullName, personalAccount.getFullName());
        assertEquals(document, personalAccount.getDocument());
        assertEquals(email, personalAccount.getEmail());
        assertEquals(password, personalAccount.getPassword());
        assertEquals(AccountType.PERSONAL, personalAccount.getAccountType());
    }
}