package com.subflow.testing;

import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.service.ChargeRequest;
import com.subflow.service.PaymentCharger;
import com.subflow.service.PaymentTimeoutException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A working, in-memory payment provider for tests. */
public class FakePaymentCharger implements PaymentCharger {
  private final Map<String, Payment> byKey = new HashMap<>();
  private final List<Payment> charges = new ArrayList<>();
  private final LocalDate today;
  private int timeoutsToSimulate;

  public FakePaymentCharger(LocalDate today) {
    this.today = today;
  }

  /** The next n calls charge the customer, then time out. */
  public FakePaymentCharger timeOutAfterCharging(int n) {
    this.timeoutsToSimulate = n;
    return this;
  }

  @Override
  public Payment charge(ChargeRequest request) {
    Payment payment = byKey.computeIfAbsent(request.idempotencyKey(), key -> {
      Payment p = new Payment("fake_" + (charges.size() + 1),
          request.subscriptionId(), PaymentProvider.PAYFAST,
          request.amount(), today);
      charges.add(p);
      return p;
    });
    if (timeoutsToSimulate > 0) {
      timeoutsToSimulate--;
      throw new PaymentTimeoutException("Simulated timeout");
    }
    return payment;
  }

  /** Every charge the customer would see on their statement. */
  public List<Payment> charges() {
    return List.copyOf(charges);
  }
}
