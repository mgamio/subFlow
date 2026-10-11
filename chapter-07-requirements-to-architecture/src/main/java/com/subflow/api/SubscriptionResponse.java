package com.subflow.api;

import com.subflow.domain.Subscription;
import java.time.LocalDate;

/** What the API returns. Independent of the domain record's shape. */
public record SubscriptionResponse(long id, long customerId, String plan,
                                   String billingCycle, String status,
                                   LocalDate startDate) {

  public static SubscriptionResponse from(Subscription s) {
    return new SubscriptionResponse(s.id(), s.customerId(), s.plan().name(),
        s.cycle().name(), s.status().name(), s.startDate());
  }
}
