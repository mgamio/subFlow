package com.subflow.service;

import static com.subflow.domain.BillingPeriods.REFUND_WINDOW_DAYS;

import com.subflow.domain.Subscription;
import java.time.Clock;
import java.time.LocalDate;

public class CancellationService {
  private final SubscriptionRepository subscriptions;
  private final Clock clock;

  public CancellationService(
      SubscriptionRepository subscriptions, Clock clock) {
    this.subscriptions = subscriptions;
    this.clock = clock;
  }

  public CancellationResult cancel(long subscriptionId) {
    Subscription subscription = subscriptions
        .findById(subscriptionId)
        .orElseThrow(() ->
            new SubscriptionNotFoundException(subscriptionId));

    LocalDate today = LocalDate.now(clock);
    LocalDate refundDeadline = subscription.startDate()
        .plusDays(REFUND_WINDOW_DAYS);
    boolean refundDue = !today.isAfter(refundDeadline);

    subscriptions.save(subscription.cancel());
    return new CancellationResult(subscriptionId, refundDue);
  }
}
