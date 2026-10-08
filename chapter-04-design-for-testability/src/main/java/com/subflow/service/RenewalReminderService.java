package com.subflow.service;

import com.subflow.domain.Subscription;
import java.time.Clock;
import java.time.LocalDate;

/** Reminds annual customers one week before their subscription renews. */
public class RenewalReminderService {
  static final int DAYS_BEFORE_RENEWAL = 7;

  private final RenewalQueries subscriptions;
  private final CustomerDirectory customers;
  private final ReminderNotifier notifier;
  private final Clock clock;

  public RenewalReminderService(RenewalQueries subscriptions,
                                CustomerDirectory customers,
                                ReminderNotifier notifier, Clock clock) {
    this.subscriptions = subscriptions;
    this.customers = customers;
    this.notifier = notifier;
    this.clock = clock;
  }

  public void sendReminders() {
    LocalDate today = LocalDate.now(clock);
    LocalDate reminderTarget = today.plusDays(DAYS_BEFORE_RENEWAL);
    for (Subscription subscription
        : subscriptions.activeAnnualSubscriptions()) {
      LocalDate renewal = subscription.nextRenewalOn(today);
      if (renewal.equals(reminderTarget)) {
        customers.findById(subscription.customerId())
            .ifPresent(customer ->
                notifier.remindRenewal(customer, renewal));
      }
    }
  }
}
