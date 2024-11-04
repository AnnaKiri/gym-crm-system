package com.annakirillova.crmsystem.health;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusHealthIndicatorUnitTest {

    private StatusHealthIndicator healthIndicator;

    @BeforeEach
    public void setUp() {
        healthIndicator = new StatusHealthIndicator();
        StatusHealthIndicator.informStatusOk();
    }

    @Test
    public void testHealthIndicatorUp() {
        Health health = healthIndicator.health();
        assertEquals("Undetected", health.getDetails().get("Errors 500"));
        assertEquals(Status.UP, health.getStatus());
    }

    @Test
    public void testHealthIndicatorDownAfterThree500s() {
        StatusHealthIndicator.informStatusBad();
        StatusHealthIndicator.informStatusBad();
        StatusHealthIndicator.informStatusBad();

        Health health = healthIndicator.health();
        assertEquals("Detected", health.getDetails().get("Errors 500"));
        assertEquals(Status.DOWN, health.getStatus());
    }
}
