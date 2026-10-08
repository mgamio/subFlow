package examples.ch04.properties.before;

import com.subflow.domain.Subscription;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** The first version: correct for subscriptions that start on the 1st. */
public final class NaiveRenewals {
  private NaiveRenewals() { }

  public static LocalDate nextRenewalOn(Subscription subscription,
                                        LocalDate day) {
    ChronoUnit unit = subscription.cycle().unit();
    long periods = unit.between(subscription.startDate(), day);
    return subscription.startDate().plus(periods + 1, unit);
  }
}
