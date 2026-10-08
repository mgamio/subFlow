package com.subflow.payment.payfast;

import static com.subflow.TestData.START;
import static com.subflow.TestData.clockOn;

import com.payfast.sdk.PayFastClient;
import com.subflow.service.PaymentCharger;
import com.subflow.testing.PaymentChargerContract;

class PayFastChargerTest extends PaymentChargerContract {
  @Override
  protected PaymentCharger charger() {
    return new PayFastPaymentAdapter(
        new PayFastClient("test-key", clockOn(START)), clockOn(START));
  }
}
