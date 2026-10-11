package com.subflow.testing;

import static com.subflow.TestData.START;

import com.subflow.service.PaymentCharger;

/** The fake must keep the same promises as the real adapters. */
class FakePaymentChargerTest extends PaymentChargerContract {
  @Override
  protected PaymentCharger charger() {
    return new FakePaymentCharger(START);
  }
}
