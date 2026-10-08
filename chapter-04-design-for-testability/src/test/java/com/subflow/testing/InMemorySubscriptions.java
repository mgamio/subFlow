package com.subflow.testing;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Subscription;
import com.subflow.domain.SubscriptionStatus;
import com.subflow.service.RenewalQueries;
import com.subflow.service.SubscriptionRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemorySubscriptions
    implements SubscriptionRepository, RenewalQueries {
  private final Map<Long, Subscription> rows = new LinkedHashMap<>();

  @Override
  public Optional<Subscription> findById(long id) {
    return Optional.ofNullable(rows.get(id));
  }

  @Override
  public void save(Subscription subscription) {
    rows.put(subscription.id(), subscription);
  }

  @Override
  public List<Subscription> activeAnnualSubscriptions() {
    List<Subscription> result = new ArrayList<>();
    for (Subscription s : rows.values()) {
      if (s.cycle() == BillingCycle.ANNUAL
          && s.status() == SubscriptionStatus.ACTIVE) {
        result.add(s);
      }
    }
    return result;
  }

  public List<Subscription> all() {
    return List.copyOf(rows.values());
  }
}
