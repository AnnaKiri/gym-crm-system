package com.annakirillova.crmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageSenderService {

    public static final String TRAINER_WORKLOAD_QUEUE = "trainer-workload";

    private final JmsTemplate jmsTemplate;

    public void sendMessage(String queueName, Object message) {
        jmsTemplate.convertAndSend(queueName, message);
    }
}

