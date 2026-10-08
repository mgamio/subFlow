package com.subflow.payment;

import com.subflow.domain.Payment;
import com.subflow.service.ChargeRequest;
import com.subflow.service.PaymentCharger;
import com.subflow.service.PaymentTimeoutException;

/**
 * Retries a charge whose outcome is unknown. Safe only because every
 * attempt sends the same idempotency key.
 */
public class RetryingPaymentCharger implements PaymentCharger {
  private final PaymentCharger delegate;
  private final RetryPolicy policy;
  private final Sleeper sleeper;

  public RetryingPaymentCharger(PaymentCharger delegate,
                                RetryPolicy policy, Sleeper sleeper) {
    this.delegate = delegate;
    this.policy = policy;
    this.sleeper = sleeper;
  }

  @Override
  public Payment charge(ChargeRequest request) {
    for (int attempt = 1; ; attempt++) {
      try {
        return delegate.charge(request);
      } catch (PaymentTimeoutException e) {
        if (attempt >= policy.maxAttempts()) {
          throw e;
        }
        sleeper.sleep(policy.delayAfter(attempt));
      }
    }
  }
}
