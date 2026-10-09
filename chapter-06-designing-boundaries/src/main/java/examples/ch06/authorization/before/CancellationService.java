package examples.ch06.authorization.before;

import com.subflow.domain.Subscription;
import com.subflow.service.SubscriptionNotFoundException;
import com.subflow.service.SubscriptionRepository;

/** Cancels any subscription whose id appears in the URL. */
public class CancellationService {
  private final SubscriptionRepository subscriptions;

  public CancellationService(SubscriptionRepository subscriptions) {
    this.subscriptions = subscriptions;
  }

  public void cancel(long subscriptionId) {
    Subscription subscription = subscriptions.findById(subscriptionId)
        .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));
    subscriptions.save(subscription.cancel());
  }
}
