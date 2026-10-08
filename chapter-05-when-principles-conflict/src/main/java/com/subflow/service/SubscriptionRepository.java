package com.subflow.service;

import com.subflow.domain.Subscription;
import java.util.Optional;

public interface SubscriptionRepository {
  Optional<Subscription> findById(long id);
  void save(Subscription subscription);
}
