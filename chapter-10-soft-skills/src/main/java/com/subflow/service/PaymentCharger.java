package com.subflow.service;

import com.subflow.domain.Payment;

public interface PaymentCharger {
  /**
   * Charges the requested amount.
   *
   * <p>Requests with the same idempotency key are charged at most
   * once; repeating a request returns the original payment.
   *
   * @throws PaymentDeclinedException the provider refused the charge
   * @throws PaymentTimeoutException the outcome is unknown; it is
   *     safe to repeat the same request
   */
  Payment charge(ChargeRequest request);
}
