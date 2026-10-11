package com.subflow.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.service.ChargeRequest;
import com.subflow.service.PaymentCharger;
import org.junit.jupiter.api.Test;

/** The promises of PaymentCharger. Every implementation must pass. */
public abstract class PaymentChargerContract {

  protected abstract PaymentCharger charger();

  private static ChargeRequest request(String key) {
    return new ChargeRequest(7, Money.usd("19.99"), key);
  }

  @Test
  void chargesTheRequestedAmount() {
    Payment payment = charger().charge(request("key-1"));
    assertEquals(Money.usd("19.99"), payment.amount());
    assertEquals(7, payment.subscriptionId());
  }

  @Test
  void repeatingARequestReturnsTheOriginalPayment() {
    PaymentCharger charger = charger();
    Payment first = charger.charge(request("key-1"));
    Payment second = charger.charge(request("key-1"));
    assertEquals(first.id(), second.id());
  }

  @Test
  void differentKeysAreDifferentCharges() {
    PaymentCharger charger = charger();
    Payment first = charger.charge(request("key-1"));
    Payment second = charger.charge(request("key-2"));
    assertNotEquals(first.id(), second.id());
  }
}
