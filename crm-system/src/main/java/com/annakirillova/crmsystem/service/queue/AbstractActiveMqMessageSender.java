package com.annakirillova.crmsystem.service.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;

@RequiredArgsConstructor
public abstract class AbstractActiveMqMessageSender implements SenderQueue {
    protected final JmsTemplate jmsTemplate;
}