package examples.ch05.abstraction;

import static com.subflow.TestData.START;
import static com.subflow.domain.BillingPeriods.REFUND_WINDOW_DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import examples.ch05.abstraction.before.CancellationRequest;
import examples.ch05.abstraction.before.RefundRules;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** The rule engine and the plain rule agree; only one is easy to read. */
class RefundRuleTest {

  @Test
  void bothVersionsGiveTheSameAnswer() {
    Subscription s = new Subscription(1, 1, Plan.PRO, BillingCycle.MONTHLY,
        SubscriptionStatus.ACTIVE, START, null);
    for (int d = 0; d < 40; d++) {
      LocalDate today = START.plusDays(d);
      boolean plain = !today.isAfter(s.startDate().plusDays(REFUND_WINDOW_DAYS));
      boolean engine = RefundRules.REFUND_DUE.isSatisfiedBy(new CancellationRequest(s, today));
      assertEquals(plain, engine, "day " + d);
    }
  }
}
