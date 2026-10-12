package com.subflow.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.subflow.testing.MovableClock;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class CircuitBreakerTest {
  private final MovableClock clock = new MovableClock();
  private final CircuitBreaker breaker = new CircuitBreaker(5, Duration.ofSeconds(30), clock);

  @Test
  void opensAfterFiveConsecutiveFailures() {
    for (int i = 0; i < 4; i++) breaker.recordFailure();
    assertTrue(breaker.allowsRequest());
    breaker.recordFailure();
    assertFalse(breaker.allowsRequest());
  }

  @Test
  void aSuccessResetsTheCount() {
    for (int i = 0; i < 4; i++) breaker.recordFailure();
    breaker.recordSuccess();
    for (int i = 0; i < 4; i++) breaker.recordFailure();
    assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
  }

  @Test
  void afterThePauseOneTrialDecides() {
    for (int i = 0; i < 5; i++) breaker.recordFailure();
    clock.advance(Duration.ofSeconds(30));
    assertTrue(breaker.allowsRequest());
    assertEquals(CircuitBreaker.State.HALF_OPEN, breaker.state());

    breaker.recordFailure();                        // the trial failed
    assertFalse(breaker.allowsRequest());

    clock.advance(Duration.ofSeconds(30));
    assertTrue(breaker.allowsRequest());
    breaker.recordSuccess();                        // the trial succeeded
    assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
  }
}
