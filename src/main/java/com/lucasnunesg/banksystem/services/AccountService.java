package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.controllers.dto.CreateAccountDto;
import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.PersonalAccount;
import com.lucasnunesg.banksystem.exceptions.AccountAlreadyExistsException;
import com.lucasnunesg.banksystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        Optional<Account> account = repository.findByEmailOrDocument(dto.email(), dto.document());
        if (account.isPresent()) {
            throw new AccountAlreadyExistsException("E-mail or Document already belong to an account");
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

    public boolean canTransfer(Long id) {
        Account account = findById(id);
        return account instanceof PersonalAccount;
    }

    public void credit(Long id, BigDecimal amount) {
        Account account = findById(id);
        account.setBalance(account.getBalance().add(amount));
        repository.save(account);
    }

    public void debit(Long id, BigDecimal amount) {
        Account account = findById(id);
        account.setBalance(account.getBalance().subtract(amount));
        repository.save(account);
    }

}
