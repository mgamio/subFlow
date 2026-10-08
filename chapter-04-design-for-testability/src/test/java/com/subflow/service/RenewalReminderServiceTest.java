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
import org.junit.jupiter.api.Test;

class RenewalReminderServiceTest {

  @Test
  void remindsSevenDaysBeforeTheAnnualRenewal() {
    InMemorySubscriptions subscriptions = new InMemorySubscriptions();
    subscriptions.save(new Subscription(1, 10, Plan.PRO, BillingCycle.ANNUAL,
        SubscriptionStatus.ACTIVE, LocalDate.of(2025, 3, 15), null));
    subscriptions.save(new Subscription(2, 20, Plan.PRO, BillingCycle.ANNUAL,
        SubscriptionStatus.ACTIVE, LocalDate.of(2025, 3, 16), null));
    List<String> sent = new ArrayList<>();

    new RenewalReminderService(subscriptions,
        id -> Optional.of(new Customer(id, "Customer " + id, id + "@example.com")),
        (customer, date) -> sent.add(customer.email() + " " + date),
        clockOn(LocalDate.of(2026, 3, 8))).sendReminders();

    assertEquals(List.of("10@example.com 2026-03-15"), sent);
  }
}
