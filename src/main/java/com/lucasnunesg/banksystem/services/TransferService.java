package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.client.dto.NotificationBodyDto;
import com.lucasnunesg.banksystem.config.RabbitMQConfig;
import com.lucasnunesg.banksystem.controllers.dto.TransferDto;
import com.lucasnunesg.banksystem.entities.Account;
import com.lucasnunesg.banksystem.entities.Transfer;
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
            AccountService accountService,
            AuthorizationService authorizationService,
            RabbitTemplate rabbitTemplate,
            TransferRepository transferRepository) {
        this.accountService = accountService;
        this.authorizationService = authorizationService;
        this.rabbitTemplate = rabbitTemplate;
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

        if (transferDto.senderId().equals(transferDto.receiverId())) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same account.");
        }

        Long senderId = transferDto.senderId();
        Long receiverId = transferDto.receiverId();

        BigDecimal amount = transferDto.amount();

        if (!accountService.canTransfer(senderId)) {
            throw new UnsupportedOperationException("Business accounts can't transfer money");
        }

        if (!checkBalance(senderId, amount)) {
            throw new UnsupportedOperationException("Insufficient balance");
        }

        if (!authorizationService.isAuthorizedTransaction()) {
            notifyUser(senderId, receiverId, false);
            throw new UnsupportedOperationException("Transaction was not authorized");
        }

        try {
            executeTransfer(senderId, receiverId, amount);
        } catch (Exception e) {
            notifyUser(senderId, receiverId, false);
        }

        notifyUser(senderId, receiverId, true);

        Account sender = accountService.findById(transferDto.senderId());
        Account receiver = accountService.findById(transferDto.receiverId());

        Transfer transfer = new Transfer(sender, receiver, amount);

        return transferRepository.save(transfer);
    }

    protected boolean checkBalance(Long accountId, BigDecimal amount) {
        Account account = accountService.findById(accountId);
        BigDecimal balance = account.getBalance();
        return amount.compareTo(balance) <= 0;
    }

    protected void executeTransfer(Long senderId, Long receiverId, BigDecimal amount) {
        try {
            accountService.debit(senderId, amount);
            accountService.credit(receiverId, amount);
        } catch (Exception e) {
            throw new UnsupportedOperationException("There was an error with the transfer");
        }
    }

    public void notifyUser(Long senderId, Long receiverId, boolean isSuccessNotification) {
        NotificationBodyDto notificationBody = new NotificationBodyDto(
                senderId,
                receiverId,
                isSuccessNotification);

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.NOTIFICATION_QUEUE, notificationBody);
    }
}
