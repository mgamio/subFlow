package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.Test;

/** Properties that must hold for every subscription and every day. */
class SubscriptionPropertiesTest {
  private static final int CASES = 10_000;
  private final Random random = new Random(42);   // fixed seed: repeatable

  @Test
  void everyDayBelongsToExactlyOnePeriod() {
    for (int i = 0; i < CASES; i++) {
      Subscription s = randomSubscription();
      LocalDate day = s.startDate().plusDays(random.nextInt(2_000));

      LocalDate start = s.periodStartOn(day);
      LocalDate next = s.nextRenewalOn(day);

      String example = s.cycle() + " from " + s.startDate() + " on " + day;
      assertFalse(start.isAfter(day), example);
      assertTrue(next.isAfter(day), example);
      assertEquals(next, s.periodStartOn(next), example);
    }
  }

  @Test
  void theRenewalIsTheSameForEveryDayOfAPeriod() {
    for (int i = 0; i < CASES; i++) {
      Subscription s = randomSubscription();
      LocalDate day = s.startDate().plusDays(random.nextInt(2_000));
      LocalDate next = s.nextRenewalOn(day);
      assertEquals(next, s.nextRenewalOn(s.periodStartOn(day)));
      assertEquals(next, s.nextRenewalOn(next.minusDays(1)));
    }
  }

  private Subscription randomSubscription() {
    LocalDate start = LocalDate.of(2024, 1, 1).plusDays(random.nextInt(1_500));
    BillingCycle cycle = random.nextBoolean() ? BillingCycle.MONTHLY : BillingCycle.ANNUAL;
    return new Subscription(1, 1, Plan.BASIC, cycle,
        SubscriptionStatus.ACTIVE, start, null);
  }
}
