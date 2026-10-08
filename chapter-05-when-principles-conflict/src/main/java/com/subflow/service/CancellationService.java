package com.subflow.service;

import static com.subflow.domain.BillingPeriods.REFUND_WINDOW_DAYS;

import com.subflow.domain.Subscription;
import java.time.Clock;
import java.time.LocalDate;

public class CancellationService {
  private final SubscriptionRepository subscriptions;
  private final PaymentRepository payments;
  private final PaymentRefunder refunder;
  private final Clock clock;

  public CancellationService(SubscriptionRepository subscriptions,
                             PaymentRepository payments,
                             PaymentRefunder refunder,
                             Clock clock) {
    this.subscriptions = subscriptions;
    this.payments = payments;
    this.refunder = refunder;
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

    RefundResult refund = null;
    if (refundDue) {
      refund = payments.lastPaymentFor(subscriptionId)
          .map(refunder::refund)
          .orElse(null);
    }
    return new CancellationResult(subscriptionId, refund);
  }
}
