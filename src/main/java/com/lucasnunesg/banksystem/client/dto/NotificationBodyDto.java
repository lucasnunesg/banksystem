package com.lucasnunesg.banksystem.client.dto;

import com.lucasnunesg.banksystem.entities.Account;

public record NotificationBodyDto(
        Account sender,
        Account receiver,
        boolean isSuccessfulTransaction
) {
}
