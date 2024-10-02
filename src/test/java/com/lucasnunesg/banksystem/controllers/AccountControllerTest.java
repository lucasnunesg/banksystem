package com.lucasnunesg.banksystem.controllers;

import com.lucasnunesg.banksystem.controllers.dto.CreateAccountDto;
import com.lucasnunesg.banksystem.entities.PersonalAccount;
import com.lucasnunesg.banksystem.entities.enums.AccountType;
import com.lucasnunesg.banksystem.services.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @InjectMocks
    AccountController controller;

    @Mock
    AccountService service;


    @Test
    void findAllShouldCallServiceCorrectly() {
        when(service.findAll()).thenReturn(new ArrayList<>());
        controller.findAll();

        verify(service, times(1)).findAll();
    }

    @Test
    void findByIdShouldCallServiceCorrectly() {
        when(service.findById(1L)).thenReturn(new PersonalAccount());
        controller.findById(1L);

        verify(service, times(1)).findById(1L);
    }

    @Test
    void createAccountShouldCallServiceCorrectly() {
        CreateAccountDto dto = new CreateAccountDto("name", "document", "email", "password", AccountType.PERSONAL);
        when(service.createAccount(dto)).thenReturn(new PersonalAccount());

        controller.createAccount(dto);

        verify(service, times(1)).createAccount(dto);
    }
}