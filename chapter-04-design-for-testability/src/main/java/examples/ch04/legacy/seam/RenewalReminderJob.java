package examples.ch04.legacy.seam;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import examples.ch04.legacy.LegacyDatabase;
import examples.ch04.legacy.MailGateway;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/** Step 2: the same logic, with every outside call behind a seam. */
public class RenewalReminderJob {

  public void run() {
    LocalDate today = today();
    for (Subscription s : loadSubscriptions()) {
      if (s.cycle() == BillingCycle.ANNUAL
          && s.status() == SubscriptionStatus.ACTIVE) {
        LocalDate renewal = s.startDate().withYear(today.getYear());
        if (renewal.isBefore(today)) {
          renewal = renewal.plusYears(1);
        }
        if (ChronoUnit.DAYS.between(today, renewal) == 7) {
          send(emailOf(s.customerId()),
              "Your SubFlow subscription renews on " + renewal + ".");
        }
      }
    }
  }

  protected LocalDate today() {
    return LocalDate.now();
  }

  protected List<Subscription> loadSubscriptions() {
    return LegacyDatabase.findAllSubscriptions();
  }

  protected String emailOf(long customerId) {
    return LegacyDatabase.findCustomerEmail(customerId);
  }

  protected void send(String to, String text) {
    MailGateway.send(to, text);
  }
}
