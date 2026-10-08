package examples.ch04.legacy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Customer;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import com.subflow.service.RenewalReminderService;
import com.subflow.testing.InMemorySubscriptions;
import examples.ch04.legacy.seam.RenewalReminderJob;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;

class CharacterizationTest {

  /** Step 2: the seams let a test replace every outside call. */
  static class TestableJob extends RenewalReminderJob {
    final List<String> sent = new ArrayList<>();
    private final LocalDate today;
    private final List<Subscription> subscriptions;

    TestableJob(LocalDate today, List<Subscription> subscriptions) {
      this.today = today;
      this.subscriptions = subscriptions;
    }

    @Override protected LocalDate today() { return today; }
    @Override protected List<Subscription> loadSubscriptions() { return subscriptions; }
    @Override protected String emailOf(long customerId) { return customerId + "@example.com"; }
    @Override protected void send(String to, String text) { sent.add(to + ": " + text); }
  }

  private static Subscription annual(long id, LocalDate start) {
    return new Subscription(id, id, Plan.PRO, BillingCycle.ANNUAL,
        SubscriptionStatus.ACTIVE, start, null);
  }

  @Test
  void step1TheOriginalJobCannotEvenRunInATest() {
    assertThrows(IllegalStateException.class,
        () -> new examples.ch04.legacy.before.RenewalReminderJob().run());
  }

  @Test
  void step2RecordWhatTheJobDoesToday() {
    TestableJob job = new TestableJob(LocalDate.of(2027, 2, 21), List.of(
        annual(1, LocalDate.of(2025, 2, 28)),
        annual(2, LocalDate.of(2024, 2, 29)),     // leap day
        annual(3, LocalDate.of(2025, 3, 1))));

    job.run();

    assertEquals(List.of(
        "1@example.com: Your SubFlow subscription renews on 2027-02-28.",
        "2@example.com: Your SubFlow subscription renews on 2027-02-28."), job.sent);
  }

  @Test
  void step4TheNewServiceBehavesLikeTheOldJob() {
    Random random = new Random(3);
    for (int i = 0; i < 2_000; i++) {
      LocalDate today = LocalDate.of(2026, 1, 1).plusDays(random.nextInt(1_100));
      List<Subscription> subscriptions = new ArrayList<>();
      for (long id = 1; id <= 20; id++) {
        LocalDate start = today.minusDays(random.nextInt(1_500));
        BillingCycle cycle = random.nextInt(4) == 0 ? BillingCycle.MONTHLY : BillingCycle.ANNUAL;
        SubscriptionStatus status = random.nextInt(5) == 0
            ? SubscriptionStatus.CANCELED : SubscriptionStatus.ACTIVE;
        subscriptions.add(new Subscription(id, id, Plan.BASIC, cycle, status, start, null));
      }

      TestableJob oldJob = new TestableJob(today, subscriptions);
      oldJob.run();

      InMemorySubscriptions store = new InMemorySubscriptions();
      subscriptions.forEach(store::save);
      List<String> sent = new ArrayList<>();
      new RenewalReminderService(store,
          id -> Optional.of(new Customer(id, "C" + id, id + "@example.com")),
          (customer, date) -> sent.add(customer.email()
              + ": Your SubFlow subscription renews on " + date + "."),
          Clock.fixed(today.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC))
          .sendReminders();

      assertEquals(oldJob.sent, sent, "today " + today);
    }
  }
}
