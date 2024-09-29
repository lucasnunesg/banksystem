package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.PersonalAccount;
import com.lucasnunesg.banksystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AccountService {

    private final AccountRepository repository;

    @Autowired
    protected AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    protected Account createAccount(Account obj) {
        return repository.save(obj);
    }


}
