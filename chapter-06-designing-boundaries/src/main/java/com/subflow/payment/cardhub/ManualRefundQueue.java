package com.subflow.payment.cardhub;

import com.subflow.domain.Payment;

/** Where support agents pick up refunds to make in CardHub's dashboard. */
public interface ManualRefundQueue {
  /** Registers a request and returns its ticket reference. */
  String request(Payment payment);
}
