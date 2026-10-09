package com.subflow.service;

import com.subflow.domain.events.DomainEvent;
import com.subflow.domain.events.SubscriptionCanceled;

/** Reacts to cancellations. Safe to receive the same event twice. */
public class RefundOnCancellation {
  private final PaymentRepository payments;
  private final PaymentRefunder refunder;
  private final ProcessedEvents processed;

  public RefundOnCancellation(PaymentRepository payments,
                              PaymentRefunder refunder,
                              ProcessedEvents processed) {
    this.payments = payments;
    this.refunder = refunder;
    this.processed = processed;
  }

  public void on(DomainEvent event) {
    if (!(event instanceof SubscriptionCanceled canceled)
        || !canceled.refundDue()) {
      return;
    }
    if (!processed.markProcessed(event.eventId())) {
      return;                                   // a duplicate delivery
    }
    payments.lastPaymentFor(canceled.subscriptionId())
        .ifPresent(refunder::refund);
  }
}
