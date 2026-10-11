package com.subflow.domain.events;

import java.time.LocalDate;

public record SubscriptionCanceled(long subscriptionId, long customerId,
                                   LocalDate occurredOn, boolean refundDue)
    implements DomainEvent {

  /** A subscription can be canceled only once, so this id is unique. */
  @Override
  public String eventId() {
    return "subscription-canceled-" + subscriptionId;
  }
}
