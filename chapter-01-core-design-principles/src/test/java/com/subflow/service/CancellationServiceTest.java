package com.subflow.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CancellationServiceTest {
  private static final LocalDate START = LocalDate.of(2026, 1, 1);
  private final InMemorySubscriptions subscriptions = new InMemorySubscriptions();

  @BeforeEach
  void storeSubscription() {
    subscriptions.save(new Subscription(1, 1, Plan.BASIC,
        BillingCycle.MONTHLY, SubscriptionStatus.ACTIVE, START, null));
  }

  @Test
  void refundIsDueOnDay14() {
    CancellationResult result = serviceOn(START.plusDays(14)).cancel(1);
    assertTrue(result.refundDue());
    assertEquals(SubscriptionStatus.CANCELED,
        subscriptions.findById(1).orElseThrow().status());
  }

  @Test
  void noRefundOnDay15() {
    assertFalse(serviceOn(START.plusDays(15)).cancel(1).refundDue());
  }

  @Test
  void unknownSubscriptionIsRejected() {
    assertThrows(SubscriptionNotFoundException.class,
        () -> serviceOn(START).cancel(99));
  }

  private CancellationService serviceOn(LocalDate day) {
    Clock clock = Clock.fixed(
        day.atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC);
    return new CancellationService(subscriptions, clock);
  }

  static class InMemorySubscriptions implements SubscriptionRepository {
    private final Map<Long, Subscription> rows = new HashMap<>();

    @Override
    public Optional<Subscription> findById(long id) {
      return Optional.ofNullable(rows.get(id));
    }

    @Override
    public void save(Subscription subscription) {
      rows.put(subscription.id(), subscription);
    }
  }
}
