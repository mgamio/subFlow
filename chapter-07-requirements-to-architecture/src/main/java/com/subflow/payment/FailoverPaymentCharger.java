package com.subflow.payment;

import com.subflow.domain.Payment;
import com.subflow.service.ChargeRequest;
import com.subflow.service.PaymentCharger;
import com.subflow.service.PaymentProviderUnavailableException;
import com.subflow.service.PaymentTimeoutException;

/**
 * Charges with the primary provider while it is healthy, and with the
 * secondary when the primary is down. Never fails over when the
 * outcome at the primary is unknown: that could charge twice.
 */
public class FailoverPaymentCharger implements PaymentCharger {
  private final PaymentCharger primary;
  private final PaymentCharger secondary;
  private final CircuitBreaker breaker;

  public FailoverPaymentCharger(PaymentCharger primary,
                                PaymentCharger secondary,
                                CircuitBreaker breaker) {
    this.primary = primary;
    this.secondary = secondary;
    this.breaker = breaker;
  }

  @Override
  public Payment charge(ChargeRequest request) {
    if (!breaker.allowsRequest()) {
      return secondary.charge(request);
    }
    try {
      Payment payment = primary.charge(request);
      breaker.recordSuccess();
      return payment;
    } catch (PaymentProviderUnavailableException e) {
      breaker.recordFailure();
      return secondary.charge(request);   // certainly not charged: safe
    } catch (PaymentTimeoutException e) {
      breaker.recordFailure();
      throw e;                            // unknown outcome: do not fail over
    }
  }
}
