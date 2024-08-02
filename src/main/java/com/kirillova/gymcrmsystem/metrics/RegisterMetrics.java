package com.kirillova.gymcrmsystem.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RegisterMetrics {

    private final Counter registerCounter;
    private final Timer registerTimerTrainee;
    private final Timer registerTimerTrainer;

    public RegisterMetrics(MeterRegistry meterRegistry) {
        this.registerCounter = Counter.builder("register.request.count")
                .description("Number of registration requests")
                .register(meterRegistry);
        this.registerTimerTrainee = Timer.builder("register.execution.time.trainee")
                .description("Time taken for registration trainee")
                .register(meterRegistry);
        this.registerTimerTrainer = Timer.builder("register.execution.time.trainer")
                .description("Time taken for registration trainer")
                .register(meterRegistry);

        this.registerCounter.increment(0);
        this.registerTimerTrainee.record(0, TimeUnit.NANOSECONDS);
        this.registerTimerTrainer.record(0, TimeUnit.NANOSECONDS);
    }

    public void incrementRequestCount() {
        registerCounter.increment();
    }

    public void recordExecutionTimeTrainee(long duration, TimeUnit unit) {
        registerTimerTrainee.record(duration, unit);
    }

    public void recordExecutionTimeTrainer(long duration, TimeUnit unit) {
        registerTimerTrainer.record(duration, unit);
    }
}
