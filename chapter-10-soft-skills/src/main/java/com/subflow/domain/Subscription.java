package com.subflow.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

  /** The first day of the billing period that contains the given day. */
  public LocalDate periodStartOn(LocalDate day) {
    return startDate.plus(periodsBefore(day), cycle.unit());
  }

  /** The first day of the next billing period: always after the given day. */
  public LocalDate nextRenewalOn(LocalDate day) {
    return startDate.plus(periodsBefore(day) + 1, cycle.unit());
  }

  /** How many whole periods have started after the start date, up to the day. */
  private long periodsBefore(LocalDate day) {
    if (day.isBefore(startDate)) {
      throw new IllegalArgumentException(
          day + " is before subscription " + id + " starts");
    }
    ChronoUnit unit = cycle.unit();
    long periods = unit.between(startDate, day);
    // Short months: Jan 31 + 1 month is Feb 28, which may already be past.
    while (!startDate.plus(periods + 1, unit).isAfter(day)) {
      periods++;
    }
    return periods;
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
