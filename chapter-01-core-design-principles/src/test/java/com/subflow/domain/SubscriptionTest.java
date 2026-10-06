package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import examples.ch01.kiss.before.SubscriptionChecker;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** Proves the KISS refactoring kept the original behavior. */
class SubscriptionTest {
  private static final LocalDate TODAY = LocalDate.of(2026, 10, 6);

  @Test
  void isActiveOnBehavesLikeTheOriginalMethod() {
    LocalDate[] endDates = { null, TODAY.plusDays(1), TODAY, TODAY.minusDays(1) };
    SubscriptionChecker original = new SubscriptionChecker();
    for (SubscriptionStatus status : SubscriptionStatus.values()) {
      for (LocalDate end : endDates) {
        Subscription s = new Subscription(1, 1, Plan.PRO,
            BillingCycle.MONTHLY, status, TODAY.minusDays(30), end);
        assertEquals(original.isActive(s, TODAY), s.isActiveOn(TODAY),
            status + " ending " + end);
      }
    }
  }
}
