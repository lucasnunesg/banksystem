package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.controllers.dto.CreateAccountDto;
import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    private final AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account createAccount(CreateAccountDto dto) {
        var account = repository.findByEmailOrDocument(dto.email(),dto.document());
        if (account.isPresent()) {
            throw new UnsupportedOperationException("Account already exists");
        }
        return repository.save(dto.toEntity());
    }

    public List<Account> findAll() {
        return repository.findAll();
    }

    public Account findById(Long id) {
        Optional<Account> obj = repository.findById(id);
        return obj.orElseThrow(() -> new IllegalArgumentException("User not found:" + id));
    }

    public Account insert(Account obj) {
        return repository.save(obj);
    }


}
