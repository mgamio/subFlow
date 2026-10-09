package com.subflow.testing;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.RenewalCandidates;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import com.subflow.service.ConcurrentUpdateException;
import com.subflow.service.RenewalQueries;
import com.subflow.service.SubscriptionRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySubscriptions
    implements SubscriptionRepository, RenewalQueries {
  private final Map<Long, Subscription> rows = new LinkedHashMap<>();

  @Override
  public synchronized Optional<Subscription> findById(long id) {
    return Optional.ofNullable(rows.get(id));
  }

  @Override
  public synchronized void save(Subscription subscription) {
    rows.put(subscription.id(), subscription);
  }

  @Override
  public synchronized void update(Subscription expected, Subscription updated) {
    if (!expected.equals(rows.get(expected.id()))) {
      throw new ConcurrentUpdateException(
          "Subscription " + expected.id() + " was changed by someone else");
    }
    rows.put(updated.id(), updated);
  }

  @Override
  public synchronized List<Subscription> activeAnnualRenewingOn(LocalDate renewalDate) {
    List<Subscription> result = new ArrayList<>();
    for (Subscription s : rows.values()) {
      if (s.cycle() == BillingCycle.ANNUAL
          && s.status() == SubscriptionStatus.ACTIVE
          && RenewalCandidates.mayRenewOn(s.startDate(), renewalDate)) {
        result.add(s);
      }
    }
    return result;
  }

  public synchronized List<Subscription> all() {
    return List.copyOf(rows.values());
  }
}
