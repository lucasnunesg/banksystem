package com.lucasnunesg.banksystem.config;

import com.lucasnunesg.banksystem.entities.BusinessAccount;
import com.lucasnunesg.banksystem.entities.PersonalAccount;
import com.lucasnunesg.banksystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DatabaseSeed implements CommandLineRunner {
    private final AccountRepository accountRepository;

    @Autowired
    public DatabaseSeed(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        PersonalAccount pa1 = new PersonalAccount(
                "Lucas",
                "01234567890",
                "lucas@gmail.com",
                "123456");

        BusinessAccount ba1 = new BusinessAccount(
                "Syndicate Software",
                "09876543210",
                "syndicate@gmail.com",
                "admin");

        accountRepository.saveAll(Arrays.asList(pa1, ba1));
    }
}
