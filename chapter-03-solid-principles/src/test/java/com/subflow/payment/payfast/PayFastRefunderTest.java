package com.subflow.payment.payfast;

import static com.subflow.TestData.START;
import static com.subflow.TestData.clockOn;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.payfast.sdk.PayFastClient;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.service.PaymentRefunder;
import com.subflow.service.PaymentRefunderContract;
import com.subflow.service.RefundStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PayFastRefunderTest extends PaymentRefunderContract {
  private final PayFastPaymentAdapter adapter = new PayFastPaymentAdapter(
      new PayFastClient("test-key", clockOn(START)), clockOn(START));

  @Override
  protected PaymentRefunder refunder() {
    return adapter;
  }

  @Override
  protected Payment validPayment() {
    return adapter.charge(7, Money.usd("19.99"));
  }

  @Test
  void payFastRefundsImmediately() {
    assertEquals(RefundStatus.REFUNDED,
        adapter.refund(validPayment()).status());
  }

  @Test
  void amountsAreSentInMinorUnitsAndReadBack() {
    adapter.charge(7, Money.usd("203.90"));
    Payment listed = adapter.paymentsBetween(START, START).get(0);
    assertEquals(Money.usd("203.90"), listed.amount());
    assertEquals(7, listed.subscriptionId());
    assertEquals(LocalDate.of(2026, 1, 1), listed.date());
  }
}
