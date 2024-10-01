package com.lucasnunesg.banksystem.client;

import com.lucasnunesg.banksystem.client.dto.NotificationBodyDto;
import com.lucasnunesg.banksystem.entities.Transfer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NotificationClient", url = "https://util.devi.tools/api/v1")
public interface NotificationClient {

    @PostMapping("/notify")
    ResponseEntity<Void> notifyUser(@RequestBody NotificationBodyDto notificationBody);
}
