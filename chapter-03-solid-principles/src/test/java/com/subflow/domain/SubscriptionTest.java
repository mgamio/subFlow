package com.subflow.domain;

import static com.subflow.TestData.START;
import static com.subflow.TestData.withStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SubscriptionTest {

  @Test
  void aTrialCanBeActivated() {
    assertEquals(SubscriptionStatus.ACTIVE,
        withStatus(SubscriptionStatus.TRIAL).activate().status());
  }

  @Test
  void aCanceledSubscriptionCannotBeActivated() {
    Subscription canceled = withStatus(SubscriptionStatus.CANCELED);
    assertThrows(IllegalStateException.class, canceled::activate);
  }

  @Test
  void aSubscriptionCannotBeCanceledTwice() {
    Subscription canceled = withStatus(SubscriptionStatus.CANCELED);
    assertThrows(IllegalStateException.class, canceled::cancel);
  }

  @Test
  void aSubscriptionCannotEndBeforeItStarts() {
    assertThrows(IllegalArgumentException.class,
        () -> new Subscription(1, 1, Plan.PRO, BillingCycle.MONTHLY,
            SubscriptionStatus.ACTIVE, START, START.minusDays(1)));
  }

  @Test
  void cancelingReturnsANewObjectAndKeepsTheOriginal() {
    Subscription active = withStatus(SubscriptionStatus.ACTIVE);
    Subscription canceled = active.cancel();
    assertEquals(SubscriptionStatus.ACTIVE, active.status());
    assertEquals(SubscriptionStatus.CANCELED, canceled.status());
  }
}
