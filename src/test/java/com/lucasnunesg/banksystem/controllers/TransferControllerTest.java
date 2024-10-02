package com.lucasnunesg.banksystem.controllers;

import com.lucasnunesg.banksystem.controllers.dto.TransferDto;
import com.lucasnunesg.banksystem.entities.Transfer;
import com.lucasnunesg.banksystem.services.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferControllerTest {

    @Mock
    private TransferService service;

    @InjectMocks
    TransferController controller;

    @Test
    void findAllShouldCallServiceCorrectly() {
        when(service.findAll()).thenReturn(new ArrayList<>());
        controller.findAll();

        verify(service, times(1)).findAll();
    }

    @Test
    void findByIdShouldCallServiceCorrectly() {
        when(service.findById(1L)).thenReturn(new Transfer());
        controller.findById(1L);

        verify(service, times(1)).findById(1L);
    }

    @Test
    void transfer() {
        TransferDto dto = new TransferDto(BigDecimal.TEN,1L,2L);

        when(service.transfer(dto)).thenReturn(new Transfer());
        controller.transfer(dto);

        verify(service, times(1)).transfer(dto);
    }
}