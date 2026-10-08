package com.subflow.service;

import com.subflow.domain.Payment;

public interface PaymentRefunder {
  /**
   * Refunds the full amount of a payment made with this provider.
   *
   * <p>Never throws for a valid payment of this provider. Returns
   * REFUNDED when the money has been returned, or PENDING_MANUAL
   * when the provider requires a manual step; in that case, a
   * request has already been registered for the support team.
   */
  RefundResult refund(Payment payment);
}
