package com.subflow.service;

import static com.subflow.domain.BillingPeriods.REFUND_WINDOW_DAYS;

import com.subflow.domain.Subscription;
import com.subflow.domain.events.SubscriptionCanceled;
import java.time.Clock;
import java.time.LocalDate;

public class CancellationService {
  private final SubscriptionRepository subscriptions;
  private final Outbox outbox;
  private final TransactionRunner transactions;
  private final Clock clock;

  public CancellationService(SubscriptionRepository subscriptions,
                             Outbox outbox,
                             TransactionRunner transactions,
                             Clock clock) {
    this.subscriptions = subscriptions;
    this.outbox = outbox;
    this.transactions = transactions;
    this.clock = clock;
  }

  public CancellationResult cancel(Caller caller, long subscriptionId) {
    Subscription subscription = subscriptions.findById(subscriptionId)
        .filter(s -> s.customerId() == caller.customerId())
        .orElseThrow(() ->
            new SubscriptionNotFoundException(subscriptionId));

    LocalDate today = LocalDate.now(clock);
    boolean refundDue = !today.isAfter(
        subscription.startDate().plusDays(REFUND_WINDOW_DAYS));
    Subscription canceled = subscription.cancel();

    transactions.inTransaction(() -> {
      subscriptions.update(subscription, canceled);
      outbox.add(new SubscriptionCanceled(subscriptionId,
          subscription.customerId(), today, refundDue));
    });
    return new CancellationResult(subscriptionId, refundDue);
  }
}
