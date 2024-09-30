package com.lucasnunesg.banksystem.client;

import com.lucasnunesg.banksystem.client.dto.AuthorizationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AuthorizationClient", url = "https://util.devi.tools/api/v2")
public interface AuthorizationClient {

    @GetMapping("/authorize")
    ResponseEntity<AuthorizationResponseDto> isAuthorizedTransaction();
}
