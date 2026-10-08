package com.subflow.service;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;

public interface PaymentCharger {
  /** Charges the amount, or throws PaymentDeclinedException. */
  Payment charge(long subscriptionId, Money amount);
}
