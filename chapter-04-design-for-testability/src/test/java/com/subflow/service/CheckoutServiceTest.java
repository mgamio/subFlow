package com.subflow.service;

import static com.subflow.TestData.START;
import static com.subflow.TestData.annualPro;
import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.Money;
import com.subflow.domain.PartnerRule;
import com.subflow.domain.Pricing;
import com.subflow.payment.RetryPolicy;
import com.subflow.payment.RetryingPaymentCharger;
import com.subflow.testing.FakePaymentCharger;
import com.subflow.testing.InMemoryPayments;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests what the customer and the business can observe. */
class CheckoutServiceTest {
  private final Pricing pricing = new Pricing(List.of(new PartnerRule()), clockOn(START));
  private final InMemoryPayments records = new InMemoryPayments();

  @Test
  void theCustomerIsChargedOnceAndThePaymentIsRecorded() {
    FakePaymentCharger payFast = new FakePaymentCharger(START);
    CheckoutService checkout =
        new CheckoutService(pricing, payFast, records, clockOn(START));

    checkout.pay(regularCustomer(), annualPro());

    assertEquals(List.of(Money.usd("203.90")),
        payFast.charges().stream().map(p -> p.amount()).toList());
    assertEquals(payFast.charges(), records.all());
  }

  @Test
  void payingTwiceInTheSamePeriodChargesOnce() {
    FakePaymentCharger payFast = new FakePaymentCharger(START);
    CheckoutService checkout =
        new CheckoutService(pricing, payFast, records, clockOn(START.plusDays(3)));

    checkout.pay(regularCustomer(), annualPro());   // double click
    checkout.pay(regularCustomer(), annualPro());

    assertEquals(1, payFast.charges().size());
    assertEquals(1, records.all().size());
  }

  @Test
  void aSlowProviderStillChargesOnce() {
    FakePaymentCharger payFast = new FakePaymentCharger(START).timeOutAfterCharging(2);
    CheckoutService checkout = new CheckoutService(pricing,
        new RetryingPaymentCharger(payFast, RetryPolicy.DEFAULT, d -> { }),
        records, clockOn(START));

    checkout.pay(regularCustomer(), annualPro());

    assertEquals(1, payFast.charges().size());
  }
}
