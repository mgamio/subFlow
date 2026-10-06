package examples.ch01.kiss.before;

import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import java.time.LocalDate;

public class SubscriptionChecker {

  public boolean isActive(Subscription subscription,
                          LocalDate today) {
    boolean active = false;
    if (subscription.status() != SubscriptionStatus.CANCELED) {
      if (subscription.endDate() == null) {
        active = true;
      } else {
        if (subscription.endDate().isAfter(today)) {
          active = true;
        } else if (subscription.endDate().isEqual(today)) {
          active = true;
        } else {
          active = false;
        }
      }
    } else {
      active = false;
    }
    return active;
  }
}
