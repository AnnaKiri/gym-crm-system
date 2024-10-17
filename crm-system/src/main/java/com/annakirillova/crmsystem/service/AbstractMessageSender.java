package com.annakirillova.crmsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;

@RequiredArgsConstructor
public abstract class AbstractMessageSender {

    protected final JmsTemplate jmsTemplate;

    public abstract void sendMessage(Object message);
}