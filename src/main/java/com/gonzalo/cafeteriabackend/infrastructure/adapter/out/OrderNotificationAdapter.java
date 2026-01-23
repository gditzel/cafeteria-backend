package com.gonzalo.cafeteriabackend.infrastructure.adapter.out;

import com.gonzalo.cafeteriabackend.application.port.out.OrderNotificationPort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationAdapter implements OrderNotificationPort {
    private final SimpMessagingTemplate messagingTemplate;

    public OrderNotificationAdapter(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notifyOrdersRefresh() {
        messagingTemplate.convertAndSend("/topic/orders", "REFRESH");
    }
}
