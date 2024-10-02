package com.lucasnunesg.banksystem.listeners;

import com.lucasnunesg.banksystem.client.NotificationClient;
import com.lucasnunesg.banksystem.client.dto.NotificationBodyDto;
import com.lucasnunesg.banksystem.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final NotificationClient notificationClient;
    private static final Logger logger = LoggerFactory.getLogger(NotificationListener.class);

    @Autowired
    public NotificationListener(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(NotificationBodyDto notificationBody) {
        notificationClient.notifyUser(notificationBody);
        logger.info("Message consumed: {}", notificationBody);
    }
}