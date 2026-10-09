package com.subflow.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class ApiErrorsTest {

  @Test
  void theOldControllerSendsItsStackTraceToTheCaller() {
    var response = new examples.ch06.errors.before.SubscriptionController()
        .cancel(7, () -> { throw new IllegalStateException("Subscription 7 is already canceled"); });
    assertEquals(500, response.status());
    assertEquals(true, response.body().contains("at examples.ch06"));
  }

  @Test
  void unexpectedFailuresRevealNothingInternal() {
    ApiError error = ApiErrors.from(new NullPointerException("customer.partner was null"));
    assertEquals(500, error.status());
    assertFalse(error.detail().contains("null"));
  }

  @Test
  void stateConflictsAre409() {
    assertEquals(409, ApiErrors.from(
        new IllegalStateException("Subscription 7 is already canceled")).status());
  }
}
