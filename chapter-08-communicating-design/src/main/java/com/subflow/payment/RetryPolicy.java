package com.subflow.payment;

import java.time.Duration;

/** How often to try, and how long to wait between tries (doubling each time). */
public record RetryPolicy(int maxAttempts, Duration firstDelay) {

  public static final RetryPolicy DEFAULT =
      new RetryPolicy(3, Duration.ofSeconds(1));

  public RetryPolicy {
    if (maxAttempts < 1) {
      throw new IllegalArgumentException("At least one attempt is needed");
    }
  }

  public Duration delayAfter(int attempt) {
    return firstDelay.multipliedBy(1L << (attempt - 1));
  }
}
