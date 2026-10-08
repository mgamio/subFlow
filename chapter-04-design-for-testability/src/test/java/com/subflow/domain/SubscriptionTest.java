package com.subflow.domain;

import static com.subflow.TestData.START;
import static com.subflow.TestData.withStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class SubscriptionTest {

  @Test
  void aCanceledSubscriptionCannotBeActivated() {
    assertThrows(IllegalStateException.class,
        () -> withStatus(SubscriptionStatus.CANCELED).activate());
  }

  @Test
  void monthlyPeriodsFollowTheStartDate() {
    Subscription s = withStatus(SubscriptionStatus.ACTIVE);   // monthly from START
    assertEquals(LocalDate.of(2026, 3, 1), s.periodStartOn(LocalDate.of(2026, 3, 20)));
    assertEquals(LocalDate.of(2026, 4, 1), s.nextRenewalOn(LocalDate.of(2026, 3, 20)));
  }

  @Test
  void aDayBeforeTheStartHasNoPeriod() {
    assertThrows(IllegalArgumentException.class,
        () -> withStatus(SubscriptionStatus.ACTIVE).periodStartOn(START.minusDays(1)));
  }
}
