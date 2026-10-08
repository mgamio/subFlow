package com.subflow.service;

import com.subflow.domain.Money;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A request to charge a subscription. Requests with the same
 * idempotency key are the same charge, however often they are sent.
 */
public record ChargeRequest(long subscriptionId, Money amount,
                            String idempotencyKey) {

  public ChargeRequest {
    Objects.requireNonNull(amount, "amount");
    Objects.requireNonNull(idempotencyKey, "idempotencyKey");
  }

  /** One charge per subscription and billing period. */
  public static ChargeRequest forBillingPeriod(long subscriptionId,
      Money amount, LocalDate periodStart) {
    return new ChargeRequest(subscriptionId, amount,
        "subscription-" + subscriptionId + "-period-" + periodStart);
  }
}
