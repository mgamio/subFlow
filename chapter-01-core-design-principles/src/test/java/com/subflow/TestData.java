package com.subflow;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Customer;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import java.time.LocalDate;

public final class TestData {
  private TestData() { }

  public static Subscription annualProSubscription() {
    return new Subscription(7, 1, Plan.PRO, BillingCycle.ANNUAL,
        SubscriptionStatus.ACTIVE, LocalDate.of(2026, 1, 1), null);
  }

  public static Customer customer() {
    return new Customer(1, "Ana Torres", "ana@example.com");
  }
}
