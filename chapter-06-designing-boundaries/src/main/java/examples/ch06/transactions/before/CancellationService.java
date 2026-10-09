package examples.ch06.transactions.before;

import com.subflow.domain.Subscription;
import com.subflow.service.PaymentRefunder;
import com.subflow.service.PaymentRepository;
import com.subflow.service.SubscriptionNotFoundException;
import com.subflow.service.SubscriptionRepository;

/** Two changes, in two systems, with nothing tying them together. */
public class CancellationService {
  private final SubscriptionRepository subscriptions;
  private final PaymentRepository payments;
  private final PaymentRefunder refunder;

  public CancellationService(SubscriptionRepository subscriptions,
                             PaymentRepository payments,
                             PaymentRefunder refunder) {
    this.subscriptions = subscriptions;
    this.payments = payments;
    this.refunder = refunder;
  }

  public void cancelWithRefund(long subscriptionId) {
    Subscription subscription = subscriptions.findById(subscriptionId)
        .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));
    subscriptions.save(subscription.cancel());
    // If the next line fails, the subscription is canceled and never refunded.
    payments.lastPaymentFor(subscriptionId).ifPresent(refunder::refund);
  }
}
