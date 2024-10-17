package com.annakirillova.crmsystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrainerMessageSender extends AbstractMessageSender {

    @Value("${spring.activemq.queues.trainer-workload}")
    private String trainerWorkloadQueue;

    public TrainerMessageSender(JmsTemplate jmsTemplate) {
        super(jmsTemplate);
    }

    @Override
    public void sendMessage(Object message) {
        jmsTemplate.convertAndSend(trainerWorkloadQueue, message);
    }
}
