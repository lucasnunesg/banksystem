package com.lucasnunesg.banksystem.services;


import com.lucasnunesg.banksystem.client.dto.NotificationBodyDto;
import com.lucasnunesg.banksystem.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public NotificationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void notifyUser(Long senderId, Long receiverId, boolean isSuccessNotification) {
        NotificationBodyDto notificationBody = new NotificationBodyDto(
                senderId,
                receiverId,
                isSuccessNotification);

        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, notificationBody);
    }

}
