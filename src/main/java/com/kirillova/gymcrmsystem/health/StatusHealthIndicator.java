package com.kirillova.gymcrmsystem.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("webAppHealth")
public class StatusHealthIndicator implements HealthIndicator {

    public static final long TIME_INTERVAL_MS = 60 * 1000;
    public static final long COUNT_FAULT_THRESHOLD = 3;

    private static int faultStatusCounter = 0;
    private static long lastTimeFaultStatus = 0;

    @Override
    public Health health() {
        if (checkCustomComponent()) {
            return Health.up().withDetail("Errors 500", "Undetected").build();
        } else {
            return Health.down().withDetail("Errors 500", "Detected").build();
        }
    }

    public boolean checkCustomComponent() {
        if (System.currentTimeMillis() - lastTimeFaultStatus > TIME_INTERVAL_MS) {
            faultStatusCounter = 0;
        }

        return faultStatusCounter < COUNT_FAULT_THRESHOLD;
    }

    public static void informStatusBad() {
        faultStatusCounter++;
        lastTimeFaultStatus = System.currentTimeMillis();
    }

    public static void informStatusOk() {
        if (faultStatusCounter < COUNT_FAULT_THRESHOLD) {
            faultStatusCounter = 0;
        }
    }
}
