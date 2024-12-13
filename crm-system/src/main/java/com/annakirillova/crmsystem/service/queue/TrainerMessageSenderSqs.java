package com.annakirillova.crmsystem.service.queue;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Profile("dev")
public class TrainerMessageSenderSqs implements SenderQueue {

    @Value("${aws.sqs.queues.trainer-workload}")
    private String trainerWorkloadQueue;

    private final SqsTemplate sqsTemplate;

    @Override
    public void sendMessage(Object message) {
        sqsTemplate.send(trainerWorkloadQueue, message);
    }
}
