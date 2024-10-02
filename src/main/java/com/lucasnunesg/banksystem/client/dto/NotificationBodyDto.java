package com.lucasnunesg.banksystem.client.dto;

public record NotificationBodyDto(
        Long payerId,
        Long payeeId,
        boolean isSuccessfulTransaction
) {
}
