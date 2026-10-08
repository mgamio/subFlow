package com.subflow.service;

import static com.subflow.TestData.clockOn;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Customer;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import com.subflow.testing.InMemorySubscriptions;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;

class RenewalReminderServiceTest {

  @Test
  void filteringCandidatesFirstSendsTheSameRemindersAsAFullScan() {
    Random random = new Random(5);
    for (int run = 0; run < 1_000; run++) {
      LocalDate today = LocalDate.of(2026, 1, 1).plusDays(random.nextInt(1_500));
      InMemorySubscriptions store = new InMemorySubscriptions();
      for (long id = 1; id <= 30; id++) {
        store.save(new Subscription(id, id, Plan.BASIC,
            random.nextInt(4) == 0 ? BillingCycle.MONTHLY : BillingCycle.ANNUAL,
            random.nextInt(5) == 0 ? SubscriptionStatus.CANCELED : SubscriptionStatus.ACTIVE,
            today.minusDays(random.nextInt(2_000)), null));
      }

      List<String> sent = new ArrayList<>();
      new RenewalReminderService(store,
          id -> Optional.of(new Customer(id, "C" + id, id + "@example.com")),
          (customer, date) -> sent.add(customer.email() + " " + date),
          clockOn(today)).sendReminders();

      List<String> expected = new ArrayList<>();
      for (Subscription s : store.all()) {
        if (s.cycle() == BillingCycle.ANNUAL && s.status() == SubscriptionStatus.ACTIVE
            && s.nextRenewalOn(today).equals(today.plusDays(7))) {
          expected.add(s.customerId() + "@example.com " + today.plusDays(7));
        }
      }
      assertEquals(expected, sent, "today " + today);
    }
  }
}
