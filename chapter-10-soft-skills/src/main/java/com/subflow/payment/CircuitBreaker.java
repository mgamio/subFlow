package com.subflow.payment;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

/**
 * Stops calling a dependency that keeps failing, and tries it again
 * after a pause. CLOSED: calls go through. OPEN: calls are skipped.
 * HALF_OPEN: one trial call decides whether to close or open again.
 */
public class CircuitBreaker {
  public enum State { CLOSED, OPEN, HALF_OPEN }

  private final int failureThreshold;
  private final Duration openFor;
  private final Clock clock;
  private State state = State.CLOSED;
  private int consecutiveFailures;
  private Instant openedAt;

  public CircuitBreaker(int failureThreshold, Duration openFor, Clock clock) {
    this.failureThreshold = failureThreshold;
    this.openFor = openFor;
    this.clock = clock;
  }

  public synchronized boolean allowsRequest() {
    switch (state) {
      case CLOSED:
        return true;
      case OPEN:
        if (clock.instant().isBefore(openedAt.plus(openFor))) {
          return false;
        }
        state = State.HALF_OPEN;
        return true;                  // this caller makes the trial call
      default:
        return false;                 // HALF_OPEN: the trial is in flight
    }
  }

  public synchronized void recordSuccess() {
    consecutiveFailures = 0;
    state = State.CLOSED;
  }

  public synchronized void recordFailure() {
    consecutiveFailures++;
    if (state == State.HALF_OPEN || consecutiveFailures >= failureThreshold) {
      state = State.OPEN;
      openedAt = clock.instant();
    }
  }

  public synchronized State state() {
    return state;
  }
}
