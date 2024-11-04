package com.annakirillova.trainerworkloadservice.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("integration-test")
public class TrainerMessageSender {
    private final JmsTemplate jmsTemplate;

    @Value("${spring.activemq.queues.trainer-workload}")
    private String trainerWorkloadQueue;

    public void sendMessage(Object message) {
        jmsTemplate.convertAndSend(trainerWorkloadQueue, message);
    }
}
