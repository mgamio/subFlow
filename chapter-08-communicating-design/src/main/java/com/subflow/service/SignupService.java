package com.subflow.service;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import java.time.Clock;
import java.time.LocalDate;
import java.util.function.LongSupplier;

public class SignupService {
  private final SubscriptionRepository subscriptions;
  private final LongSupplier ids;
  private final Clock clock;

  public SignupService(SubscriptionRepository subscriptions,
                       LongSupplier ids, Clock clock) {
    this.subscriptions = subscriptions;
    this.ids = ids;
    this.clock = clock;
  }

  public Subscription signUp(long customerId, Plan plan, BillingCycle cycle) {
    Subscription subscription = new Subscription(ids.getAsLong(),
        customerId, plan, cycle, SubscriptionStatus.ACTIVE,
        LocalDate.now(clock), null);
    subscriptions.save(subscription);
    return subscription;
  }
}
