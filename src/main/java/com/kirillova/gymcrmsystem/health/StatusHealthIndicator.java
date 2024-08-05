package com.kirillova.gymcrmsystem.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component("webAppHealth")
public class StatusHealthIndicator implements HealthIndicator {

    public static final long TIME_INTERVAL_MS = 60 * 1000;
    public static final long COUNT_FAULT_THRESHOLD = 3;

    private static final AtomicInteger faultStatusCounter = new AtomicInteger(0);
    private static final AtomicLong lastTimeFaultStatus = new AtomicLong(0);

    @Override
    public Health health() {
        if (checkCustomComponent()) {
            return Health.up().withDetail("Errors 500", "Undetected").build();
        } else {
            return Health.down().withDetail("Errors 500", "Detected").build();
        }
    }

    public boolean checkCustomComponent() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimeFaultStatus.get() > TIME_INTERVAL_MS) {
            faultStatusCounter.set(0);
        }

        return faultStatusCounter.get() < COUNT_FAULT_THRESHOLD;
    }

    public static void informStatusBad() {
        faultStatusCounter.incrementAndGet();
        lastTimeFaultStatus.set(System.currentTimeMillis());
    }

    public static void informStatusOk() {
        if (faultStatusCounter.get() < COUNT_FAULT_THRESHOLD) {
            faultStatusCounter.set(0);
        }
    }
}
