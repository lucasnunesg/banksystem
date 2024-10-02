package com.lucasnunesg.banksystem.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransferEntityTest {

    @Test
    void testTransferConstructor() {
        Account payer = new Account() {};
        Account payee = new Account() {};
        BigDecimal value = BigDecimal.TEN;

        Transfer transfer = new Transfer(payer, payee, value);

        assertEquals(payer,transfer.getPayer());
        assertEquals(payee,transfer.getPayee());
        assertEquals(value,transfer.getValue());
    }

}