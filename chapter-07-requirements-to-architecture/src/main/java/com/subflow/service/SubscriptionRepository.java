package com.subflow.service;

import com.subflow.domain.Subscription;
import java.util.Optional;

public interface SubscriptionRepository {
  Optional<Subscription> findById(long id);

  void save(Subscription subscription);

  /**
   * Replaces {@code expected} with {@code updated}, but only if the stored
   * subscription is still {@code expected}.
   *
   * @throws ConcurrentUpdateException if it changed in the meantime
   */
  void update(Subscription expected, Subscription updated);
}
