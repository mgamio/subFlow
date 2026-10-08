package com.subflow.payment;

import java.time.Duration;

/** Waiting, made replaceable: tests pass a sleeper that returns at once. */
@FunctionalInterface
public interface Sleeper {
  void sleep(Duration duration);

  Sleeper REAL = duration -> {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while waiting", e);
    }
  };
}
