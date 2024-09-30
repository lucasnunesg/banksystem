package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.controllers.dto.TransferDto;
import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.Transfer;
import com.lucasnunesg.banksystem.repositories.AccountRepository;
import com.lucasnunesg.banksystem.repositories.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private final AccountService accountService;

    private final TransferRepository transferRepository;

    @Autowired
    protected TransferService(
            AccountRepository accountRepository,
            AccountService accountService,
            TransferRepository transferRepository) {
        this.accountService = accountService;
        this.transferRepository = transferRepository;
    }

    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    public Transfer findById(Long id) {
        Optional<Transfer> obj = transferRepository.findById(id);
        return obj.orElseThrow(() -> new IllegalArgumentException("Transfer not found:" + id));
    }

    @Transactional
    public Transfer transfer(TransferDto transferDto) {
        Account sender = accountService.findById(transferDto.senderId());
        Account receiver = accountService.findById(transferDto.receiverId());

        Long payeeId = sender.getId();
        BigDecimal amount = transferDto.amount();

        if (!canTransfer(payeeId)) {
            throw new UnsupportedOperationException("Business accounts can't transfer money");
        }

        if (!checkBalance(payeeId, amount)) {
            throw new UnsupportedOperationException("Insufficient balance");
        }

        if (!authorizeTransaction()) {
            notifyUser(payeeId, false);
            throw new UnsupportedOperationException("Transaction was not authorized");
        }
        try {
            executeTransfer(payeeId, amount);
        } catch (Exception e) {
            notifyUser(payeeId, false);
        }
        notifyUser(payeeId, true);

        return new Transfer(sender, receiver,amount);
    }

    protected boolean canTransfer(Long accountId){
        return accountService.canTransfer(accountId);
    }

    protected boolean checkBalance(Long accountId, BigDecimal amount) {
        Account account = accountService.findById(accountId);
        BigDecimal balance = account.getBalance();
        return amount.compareTo(balance) <= 0;
    }

    protected boolean authorizeTransaction() {
        // Call to transaction service to see if it has authorization
        return true;
    }

    protected void executeTransfer(Long accountId, BigDecimal amount) {
        // Call to transaction service to perform transfer
        System.out.println("Transfer executed successfully. Details:");
        System.out.println("Account id: " + accountId);
        System.out.println("Amount: " + amount);
    }

    protected void notifyUser(Long accountId, boolean isSuccessNotification) {
        // Call to notification service to queue message
        System.out.println("Notification scheduled. Details:");
        System.out.println("Account id: " + accountId);
        System.out.println("Success: " + isSuccessNotification);


    }
}
