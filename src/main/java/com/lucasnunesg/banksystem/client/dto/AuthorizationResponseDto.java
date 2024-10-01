package com.lucasnunesg.banksystem.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthorizationResponseDto(
        @JsonProperty("status") String status,
        @JsonProperty("data") Data data) {

    public record Data(
            @JsonProperty("authorization") Boolean authorization){}
}
