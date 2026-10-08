package examples.ch04.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import examples.ch04.properties.before.NaiveRenewals;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;

class NaiveRenewalsTest {

  private static Subscription monthlyFrom(LocalDate start) {
    return new Subscription(1, 1, Plan.BASIC, BillingCycle.MONTHLY,
        SubscriptionStatus.ACTIVE, start, null);
  }

  @Test
  void exampleTestsPass() {
    Subscription s = monthlyFrom(LocalDate.of(2026, 1, 1));
    assertEquals(LocalDate.of(2026, 2, 1),
        NaiveRenewals.nextRenewalOn(s, LocalDate.of(2026, 1, 15)));
    assertEquals(LocalDate.of(2026, 3, 1),
        NaiveRenewals.nextRenewalOn(s, LocalDate.of(2026, 2, 1)));
  }

  @Test
  void thePropertyFindsACounterexample() {
    Random random = new Random(42);
    Optional<String> counterexample = Optional.empty();
    for (int i = 0; i < 10_000 && counterexample.isEmpty(); i++) {
      LocalDate start = LocalDate.of(2024, 1, 1).plusDays(random.nextInt(1_500));
      LocalDate day = start.plusDays(random.nextInt(2_000));
      LocalDate next = NaiveRenewals.nextRenewalOn(monthlyFrom(start), day);
      if (!next.isAfter(day)) {
        counterexample = Optional.of("start " + start + ", day " + day + ", next " + next);
      }
    }
    assertTrue(counterexample.isPresent());
    System.out.println("Counterexample: " + counterexample.get());
  }

  @Test
  void theSmallestCounterexample() {
    Subscription s = monthlyFrom(LocalDate.of(2026, 1, 31));
    LocalDate day = LocalDate.of(2026, 2, 28);
    assertEquals(day, NaiveRenewals.nextRenewalOn(s, day));   // should be after the day
    assertEquals(LocalDate.of(2026, 3, 31), s.nextRenewalOn(day));
  }
}
