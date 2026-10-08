package com.subflow.payment.cardhub;

import static com.subflow.TestData.START;
import static com.subflow.TestData.clockOn;

import com.subflow.service.PaymentCharger;
import com.subflow.testing.PaymentChargerContract;

class CardHubChargerTest extends PaymentChargerContract {
  @Override
  protected PaymentCharger charger() {
    return new CardHubPaymentAdapter(payment -> "TICKET-1", clockOn(START));
  }
}
