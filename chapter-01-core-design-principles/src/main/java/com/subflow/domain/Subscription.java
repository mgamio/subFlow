package com.subflow.domain;

import java.time.LocalDate;

public record Subscription(
    long id,
    long customerId,
    Plan plan,
    BillingCycle cycle,
    SubscriptionStatus status,
    LocalDate startDate,
    LocalDate endDate) {     // null = renews automatically

  public boolean isActiveOn(LocalDate day) {
    if (status == SubscriptionStatus.CANCELED) {
      return false;
    }
    return endDate == null || !endDate.isBefore(day);
  }

  public Subscription withStatus(SubscriptionStatus newStatus) {
    return new Subscription(id, customerId, plan, cycle,
        newStatus, startDate, endDate);
  }
}
