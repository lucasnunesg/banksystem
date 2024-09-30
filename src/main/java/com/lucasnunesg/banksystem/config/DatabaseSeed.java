package com.lucasnunesg.banksystem.config;

import com.lucasnunesg.banksystem.entities.BusinessAccount;
import com.lucasnunesg.banksystem.entities.PersonalAccount;
import com.lucasnunesg.banksystem.entities.Transfer;
import com.lucasnunesg.banksystem.repositories.AccountRepository;
import com.lucasnunesg.banksystem.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Arrays;

@Configuration
public class DatabaseSeed implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Autowired
    public DatabaseSeed(AccountRepository accountRepository, TransferRepository transferRepository){
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Override
    public void run(String... args) {
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

        ba1.setBalance(BigDecimal.valueOf(5000.00));
        pa1.setBalance(BigDecimal.valueOf(3000.00));

        Transfer t1 = new Transfer(pa1,ba1,BigDecimal.valueOf(200.00));
        Transfer t2 = new Transfer(pa1,ba1,BigDecimal.valueOf(500.00));

        accountRepository.saveAll(Arrays.asList(pa1, ba1));
        transferRepository.saveAll(Arrays.asList(t1,t2));
    }
}
