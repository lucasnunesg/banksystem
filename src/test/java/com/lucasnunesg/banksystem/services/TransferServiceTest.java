package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.controllers.dto.TransferDto;
import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.Transfer;
import com.lucasnunesg.banksystem.exceptions.ExternalServiceUnavailableException;
import com.lucasnunesg.banksystem.exceptions.FailedTransferException;
import com.lucasnunesg.banksystem.exceptions.ResourceNotFoundException;
import com.lucasnunesg.banksystem.exceptions.UnauthorizedTransactionException;
import com.lucasnunesg.banksystem.repositories.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransferServiceTest {
    @Mock
    private AccountService accountService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferService transferService;

    public TransferServiceTest () {
        MockitoAnnotations.openMocks(this);
    }

    private TransferDto transferDto;
    private Account account;

    @BeforeEach
    void setup() {
        transferDto = new TransferDto(BigDecimal.ONE, 1L, 2L);
        account = new Account(){};
    }


    @Test
    void testFindByIdCallsRepository() {
        Long id = transferDto.payer();
        Transfer transfer = new Transfer();
        when(transferRepository.findById(id)).thenReturn(Optional.of(transfer));

        Transfer result = transferService.findById(id);

        verify(transferRepository).findById(id);
        assertEquals(transfer, result);
    }

   @Test
    void testFindByIdThrowsExceptionWhenNotFound() {
        when(transferRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transferService.findById(1L));
    }

    @Test
    void testTransferThrowsFailedTransferExceptionWhenSameAccount() {
        TransferDto invalidTransferDto = new TransferDto(BigDecimal.ONE, 1L, 1L);
        assertThrows(FailedTransferException.class, () -> transferService.transfer(invalidTransferDto));
    }

    @Test
    void testTransferThrowsUnauthorizedTransactionExceptionWhenCanTransferFalse() {
        when(accountService.canTransfer(1L)).thenReturn(false);

        assertThrows(UnauthorizedTransactionException.class, () -> transferService.transfer(transferDto));
    }

    @Test
    void testTransferThrowsUnauthorizedTransactionExceptionWhenInsufficientBalance() {
        when(accountService.canTransfer(transferDto.payer())).thenReturn(true);
        when(accountService.findById(transferDto.payer())).thenReturn(account);

        assertThrows(UnauthorizedTransactionException.class, () -> transferService.transfer(transferDto));
    }

    @Test
    void testTransferThrowsUnauthorizedTransactionExceptionWhenNotAuthorized() {
        when(accountService.canTransfer(transferDto.payer())).thenReturn(true);
        when(accountService.findById(transferDto.payer())).thenReturn(account);
        when(authorizationService.isAuthorizedTransaction()).thenReturn(false);

        assertThrows(UnauthorizedTransactionException.class, () -> transferService.transfer(transferDto));
    }

    @Test
    void testTransferThrowsExternalServiceUnavailableExceptionAndNotifiesUser() {
        account.setBalance(BigDecimal.TEN);
        when(accountService.canTransfer(1L)).thenReturn(true);
        when(accountService.findById(1L)).thenReturn(account);
        when(authorizationService.isAuthorizedTransaction()).thenThrow(new ExternalServiceUnavailableException("Service unavailable"));

        assertThrows(ExternalServiceUnavailableException.class, () -> transferService.transfer(transferDto));
        verify(notificationService).notifyUser(1L, 2L, false);
    }

    @Test
    void testTransferNotifiesUserOnFailure() {
        account.setBalance(BigDecimal.TEN);
        when(accountService.canTransfer(1L)).thenReturn(true);
        when(accountService.findById(1L)).thenReturn(account);
        when(authorizationService.isAuthorizedTransaction()).thenReturn(true);
        doThrow(new RuntimeException("Transfer failed")).when(accountService).debit(any(), any());

        assertThrows(RuntimeException.class, () -> transferService.transfer(transferDto));
        verify(notificationService).notifyUser(1L, 2L, false);
    }

    @Test
    void testTransferNotifiesUserOnSuccess() {
        Account payer = new Account() {};
        payer.setBalance(BigDecimal.TEN);
        Account payee = new Account() {};
        when(accountService.canTransfer(1L)).thenReturn(true);
        when(accountService.findById(1L)).thenReturn(payer);
        when(accountService.findById(2L)).thenReturn(payee);
        when(authorizationService.isAuthorizedTransaction()).thenReturn(true);
        when(transferRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Transfer transfer = transferService.transfer(transferDto);

        verify(notificationService).notifyUser(1L, 2L, true);
        verify(transferRepository).save(any(Transfer.class));
        assertEquals(payer, transfer.getPayer());
        assertEquals(payee, transfer.getPayee());
        assertEquals(transferDto.value(), transfer.getValue());
    }

    @Test
    void testCheckBalanceReturnsTrueWhenSufficient() {
        Long accountId = 1L;
        BigDecimal amount = BigDecimal.TEN;

        account.setBalance(BigDecimal.valueOf(20));

        when(accountService.findById(accountId)).thenReturn(account);

        boolean result = transferService.checkBalance(accountId, amount);

        assertTrue(result);
    }

    @Test
    void testCheckBalanceReturnsFalseWhenInsufficient() {
        Long accountId = transferDto.payer();
        BigDecimal amount = BigDecimal.valueOf(30);

        when(accountService.findById(accountId)).thenReturn(account);

        boolean result = transferService.checkBalance(accountId, amount);

        assertFalse(result);
    }

    @Test
    void testExecuteTransfer() {
        Long payerId = transferDto.payer();
        Long payeeId = transferDto.payee();
        BigDecimal amount = BigDecimal.TEN;

        Account payer = new Account(){};
        Account payee = new Account(){};

        when(accountService.findById(payerId)).thenReturn(payer);
        when(accountService.findById(payeeId)).thenReturn(payee);

        transferService.executeTransfer(payerId, payeeId, amount);

        verify(accountService).debit(payerId, amount);
        verify(accountService).credit(payeeId, amount);
    }

    @Test
    void testExecuteTransferThrowsFailedTransferException() {
        Long payerId = transferDto.payer();
        Long payeeId = transferDto.payee();
        BigDecimal amount = BigDecimal.TEN;

        doThrow(new RuntimeException("Debit failed")).when(accountService).debit(any(), any());

        assertThrows(FailedTransferException.class, () -> transferService.executeTransfer(payerId, payeeId, amount));
        verify(accountService).debit(payerId, amount);
        verify(accountService, never()).credit(any(), any());
    }
}
