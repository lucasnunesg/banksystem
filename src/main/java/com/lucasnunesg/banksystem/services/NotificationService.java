package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.client.NotificationClient;
import com.lucasnunesg.banksystem.client.dto.NotificationBodyDto;
import com.lucasnunesg.banksystem.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationClient notificationClient;

    @Autowired
    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(NotificationBodyDto notificationBody) {
        notificationClient.notifyUser(notificationBody);
    }
}
