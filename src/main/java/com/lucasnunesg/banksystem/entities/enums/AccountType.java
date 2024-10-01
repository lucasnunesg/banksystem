package com.lucasnunesg.banksystem.entities.enums;

import com.lucasnunesg.banksystem.exceptions.InvalidAccountTypeException;

public enum AccountType {

    PERSONAL(1),
    BUSINESS(2);

    private final int code;

    AccountType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AccountType valueOf(int code){
        for (AccountType account : AccountType.values()) {
            if (account.getCode() == code) {
                return account;
            }
        }
        throw new InvalidAccountTypeException("Account type not supported");
    }
}
