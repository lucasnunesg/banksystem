package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.controllers.dto.TransferDto;
import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.Transfer;
import com.lucasnunesg.banksystem.exceptions.ExternalServiceUnavailableException;
import com.lucasnunesg.banksystem.exceptions.FailedTransferException;
import com.lucasnunesg.banksystem.exceptions.ResourceNotFoundException;
import com.lucasnunesg.banksystem.exceptions.UnauthorizedTransactionException;
import com.lucasnunesg.banksystem.repositories.TransferRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private final AccountService accountService;
    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;
    private final TransferRepository transferRepository;

    private final Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    protected TransferService(
            AccountService accountService,
            AuthorizationService authorizationService,
            NotificationService notificationService,
            TransferRepository transferRepository) {
        this.accountService = accountService;
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
        this.transferRepository = transferRepository;
    }

    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    public Transfer findById(Long id) {
        Optional<Transfer> obj = transferRepository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException("Transfer not found:" + id));
    }

    @Transactional
    public Transfer transfer(TransferDto transferDto) {

        Long payerId = transferDto.payer();
        Long payeeId = transferDto.payee();

        if (payerId.equals(payeeId)) {
            throw new FailedTransferException("Sender and receiver cannot be the same account");
        }

        BigDecimal amount = transferDto.value();

        if (!accountService.canTransfer(payerId)) {
            throw new UnauthorizedTransactionException("Business accounts can't transfer money");
        }

        if (!checkBalance(payerId, amount)) {
            throw new UnauthorizedTransactionException("Insufficient balance");
        }

        try {
            boolean isAuthorized = authorizationService.isAuthorizedTransaction();
            if (!isAuthorized) {
                notificationService.notifyUser(payerId, payeeId, false);
                throw new UnauthorizedTransactionException("Transaction was not authorized by external service");
            }
        } catch (ExternalServiceUnavailableException e) {
            notificationService.notifyUser(payerId, payeeId, false);
            throw e;
        }

        try {
            executeTransfer(payerId, payeeId, amount);
        } catch (Exception e) {
            notificationService.notifyUser(payerId, payeeId, false);
            throw e;
        }

        logger.info("Transfer is complete, now sending notification");
        notificationService.notifyUser(payerId, payeeId, true);

        Account payer = accountService.findById(transferDto.payer());
        Account payee = accountService.findById(transferDto.payee());

        Transfer transfer = new Transfer(payer, payee, amount);

        return transferRepository.save(transfer);
    }

    protected boolean checkBalance(Long accountId, BigDecimal amount) {
        Account account = accountService.findById(accountId);
        BigDecimal balance = account.getBalance();
        return amount.compareTo(balance) <= 0;
    }

    protected void executeTransfer(Long payerId, Long payeeId, BigDecimal amount) {
        try {
            accountService.debit(payerId, amount);
            accountService.credit(payeeId, amount);
        } catch (Exception e) {
            throw new FailedTransferException(
                    String.format("Unable to move balance between accounts %s and %s", payerId, payeeId));
        }
    }
}
