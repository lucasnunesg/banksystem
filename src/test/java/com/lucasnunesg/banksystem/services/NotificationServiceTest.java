package com.lucasnunesg.banksystem.services;

import com.lucasnunesg.banksystem.client.dto.NotificationBodyDto;
import com.lucasnunesg.banksystem.config.RabbitMQConfig;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private NotificationService notificationService;

    public NotificationServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testNotifyUserCallsConvertAndSend() {
        Long payerId = 1L;
        Long payeeId = 2L;
        boolean isSuccessNotification = true;

        notificationService.notifyUser(payerId, payeeId, isSuccessNotification);

        ArgumentCaptor<NotificationBodyDto> captor = ArgumentCaptor.forClass(NotificationBodyDto.class);
        verify(rabbitTemplate, times(1)).convertAndSend(eq(RabbitMQConfig.NOTIFICATION_QUEUE), captor.capture());

    }
}
