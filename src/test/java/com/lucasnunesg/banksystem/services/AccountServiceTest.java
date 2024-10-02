package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.controllers.dto.CreateAccountDto;
import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.PersonalAccount;
import com.lucasnunesg.banksystem.entities.enums.AccountType;
import com.lucasnunesg.banksystem.exceptions.AccountAlreadyExistsException;
import com.lucasnunesg.banksystem.exceptions.ResourceNotFoundException;
import com.lucasnunesg.banksystem.repositories.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    private CreateAccountDto dto;
    private Account account;

    @BeforeEach
    void setUp() {
        String fullName = "Test Name";
        String document = "test_document";
        String email = "test@example.com";
        String password = "password";
        AccountType accountType = AccountType.PERSONAL;
        dto = new CreateAccountDto(fullName, document, email, password, accountType);
        account = new PersonalAccount(fullName, document, email, password);
        account.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void createAccountShouldThrowExceptionIfAccountAlreadyExists() {
        when(accountRepository.findByEmailOrDocument(dto.email(), dto.document())).thenReturn(Optional.of(account));

        assertThrows(AccountAlreadyExistsException.class, () -> accountService.createAccount(dto));

        verify(accountRepository, times(1)).findByEmailOrDocument(dto.email(), dto.document());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void createAccountShouldSaveAccountIfItDoesNotAlreadyExist() {
        when(accountRepository.findByEmailOrDocument(dto.email(), dto.document())).thenReturn(Optional.empty());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.createAccount(dto);

        verify(accountRepository, times(1)).findByEmailOrDocument(dto.email(), dto.document());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void findAllShouldReturnListOfAccounts() {
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.findAll();

        assertEquals(accounts, result);
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void findByIdShouldReturnAccountIfExists() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.findById(1L);

        assertEquals(account, result);
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionIfAccountDoesNotExist() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.findById(1L));

        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void creditShouldIncreaseAccountBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        BigDecimal value = BigDecimal.TEN;
        BigDecimal balance = account.getBalance();
        BigDecimal result = balance.add(value);

        accountService.credit(1L, value);

        assertEquals(result, account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void debitShouldDecreaseAccountBalance() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        BigDecimal value = BigDecimal.TEN;
        BigDecimal balance = account.getBalance();
        BigDecimal result = balance.subtract(value);

        accountService.debit(1L, value);

        assertEquals(result, account.getBalance());
        verify(accountRepository, times(1)).save(account);
    }
}
