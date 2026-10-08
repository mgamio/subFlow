package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Random;
import org.junit.jupiter.api.Test;

/** The cheap SQL filter must never drop a real renewal. */
class RenewalCandidatesPropertiesTest {
  private final Random random = new Random(11);

  @Test
  void everyRealRenewalIsACandidate() {
    for (int i = 0; i < 20_000; i++) {
      LocalDate start = LocalDate.of(2020, 1, 1).plusDays(random.nextInt(3_000));
      LocalDate day = start.plusDays(random.nextInt(3_000));
      Subscription s = new Subscription(1, 1, Plan.PRO, BillingCycle.ANNUAL,
          SubscriptionStatus.ACTIVE, start, null);
      LocalDate renewal = s.nextRenewalOn(day);
      assertTrue(RenewalCandidates.mayRenewOn(start, renewal),
          "start " + start + ", renewal " + renewal);
    }
  }
}
