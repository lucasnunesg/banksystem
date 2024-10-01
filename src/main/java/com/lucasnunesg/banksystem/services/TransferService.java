package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.client.dto.NotificationBodyDto;
import com.lucasnunesg.banksystem.config.RabbitMQConfig;
import com.lucasnunesg.banksystem.controllers.dto.TransferDto;
import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.Transfer;
import com.lucasnunesg.banksystem.repositories.AccountRepository;
import com.lucasnunesg.banksystem.repositories.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class TransferService {

    private final AccountService accountService;
    private final AuthorizationService authorizationService;
    private final RabbitTemplate rabbitTemplate;
    private final TransferRepository transferRepository;

    @Autowired
    protected TransferService(
            AccountRepository accountRepository,
            AccountService accountService,
            AuthorizationService authorizationService,
            RabbitTemplate rabbitTemplate,
            TransferRepository transferRepository) {
        this.accountService = accountService;
        this.transferRepository = transferRepository;
        this.authorizationService = authorizationService;
        this.rabbitTemplate = rabbitTemplate;
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

        if (transferDto.senderId().equals(transferDto.receiverId())) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same account.");
        }

        Account sender = accountService.findById(transferDto.senderId());
        Account receiver = accountService.findById(transferDto.receiverId());

        Long senderId = sender.getId();
        BigDecimal amount = transferDto.amount();

        if (!canTransfer(senderId)) {
            throw new UnsupportedOperationException("Business accounts can't transfer money");
        }

        if (!checkBalance(senderId, amount)) {
            throw new UnsupportedOperationException("Insufficient balance");
        }

        if (!authorizationService.isAuthorizedTransaction()) {
            notifyUser(sender, receiver, false);
            throw new UnsupportedOperationException("Transaction was not authorized");
        }

        try {
            executeTransfer(senderId, amount);
        } catch (Exception e) {
            notifyUser(sender, receiver, false);
        }
        notifyUser(sender, receiver, true);

        accountService.credit(receiver.getId(),amount);
        accountService.debit(sender.getId(), amount);

        Transfer transfer = new Transfer(sender, receiver, amount);

        return transferRepository.save(transfer);
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
    }

    public void notifyUser(Account sender, Account receiver, boolean isSuccessNotification) {
        NotificationBodyDto notificationBody = new NotificationBodyDto(
                sender,
                receiver,
                isSuccessNotification);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.NOTIFICATION_QUEUE, notificationBody);
    }
}
