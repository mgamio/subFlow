package com.subflow.payment;

import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.service.PaymentRefunder;
import com.subflow.service.RefundResult;
import java.util.Map;

/** Sends each refund to the provider that took the payment. */
public class RoutingPaymentRefunder implements PaymentRefunder {
  private final Map<PaymentProvider, PaymentRefunder> refunders;

  public RoutingPaymentRefunder(
      Map<PaymentProvider, PaymentRefunder> refunders) {
    this.refunders = Map.copyOf(refunders);
  }

  @Override
  public RefundResult refund(Payment payment) {
    PaymentRefunder refunder = refunders.get(payment.provider());
    if (refunder == null) {
      throw new IllegalStateException(
          "No refunder configured for " + payment.provider());
    }
    return refunder.refund(payment);
  }
}
