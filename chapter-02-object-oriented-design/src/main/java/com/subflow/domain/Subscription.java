package com.subflow.domain;

import java.time.LocalDate;
import java.util.Objects;

public record Subscription(
    long id,
    long customerId,
    Plan plan,
    BillingCycle cycle,
    SubscriptionStatus status,
    LocalDate startDate,
    LocalDate endDate) {     // null = renews automatically

  public Subscription {
    Objects.requireNonNull(plan, "plan");
    Objects.requireNonNull(cycle, "cycle");
    Objects.requireNonNull(status, "status");
    Objects.requireNonNull(startDate, "startDate");
    if (endDate != null && endDate.isBefore(startDate)) {
      throw new IllegalArgumentException(
          "Subscription " + id + " ends before it starts");
    }
  }

  public boolean isActiveOn(LocalDate day) {
    if (status == SubscriptionStatus.CANCELED) {
      return false;
    }
    return endDate == null || !endDate.isBefore(day);
  }

  public Subscription activate() {
    if (status != SubscriptionStatus.TRIAL) {
      throw new IllegalStateException("Only a trial can be "
          + "activated; subscription " + id + " is " + status);
    }
    return withStatus(SubscriptionStatus.ACTIVE);
  }

  public Subscription cancel() {
    if (status == SubscriptionStatus.CANCELED) {
      throw new IllegalStateException(
          "Subscription " + id + " is already canceled");
    }
    return withStatus(SubscriptionStatus.CANCELED);
  }

  private Subscription withStatus(SubscriptionStatus newStatus) {
    return new Subscription(id, customerId, plan, cycle,
        newStatus, startDate, endDate);
  }
}
