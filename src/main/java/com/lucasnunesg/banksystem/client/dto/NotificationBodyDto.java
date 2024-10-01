package com.lucasnunesg.banksystem.client.dto;

public record NotificationBodyDto(
        Long senderId,
        Long receiverId,
        boolean isSuccessfulTransaction
) {
}
