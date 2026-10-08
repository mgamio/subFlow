package examples.ch04.legacy.before;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import examples.ch04.legacy.LegacyDatabase;
import examples.ch04.legacy.MailGateway;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/** Runs every night. Nobody has dared to change it in three years. */
public class RenewalReminderJob {

  public void run() {
    LocalDate today = LocalDate.now();
    for (Subscription s : LegacyDatabase.findAllSubscriptions()) {
      if (s.cycle() == BillingCycle.ANNUAL
          && s.status() == SubscriptionStatus.ACTIVE) {
        LocalDate renewal = s.startDate().withYear(today.getYear());
        if (renewal.isBefore(today)) {
          renewal = renewal.plusYears(1);
        }
        if (ChronoUnit.DAYS.between(today, renewal) == 7) {
          String email = LegacyDatabase.findCustomerEmail(s.customerId());
          MailGateway.send(email,
              "Your SubFlow subscription renews on " + renewal + ".");
        }
      }
    }
  }
}
