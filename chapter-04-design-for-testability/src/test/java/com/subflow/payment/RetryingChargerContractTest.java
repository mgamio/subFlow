package com.subflow.payment;

import static com.subflow.TestData.START;

import com.subflow.service.PaymentCharger;
import com.subflow.testing.FakePaymentCharger;
import com.subflow.testing.PaymentChargerContract;

/** A decorator is also an implementation: it must keep the contract. */
class RetryingChargerContractTest extends PaymentChargerContract {
  @Override
  protected PaymentCharger charger() {
    return new RetryingPaymentCharger(new FakePaymentCharger(START),
        RetryPolicy.DEFAULT, duration -> { });
  }
}
