package com.annakirillova.crmsystem.service.queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("!dev")
public class TrainerMessageSenderActiveMQ extends AbstractActiveMqMessageSender {

    @Value("${spring.activemq.queues.trainer-workload}")
    private String trainerWorkloadQueue;

    public TrainerMessageSenderActiveMQ(JmsTemplate jmsTemplate) {
        super(jmsTemplate);
    }

    @Override
    public void sendMessage(Object message) {
        jmsTemplate.convertAndSend(trainerWorkloadQueue, message);
    }
}
