package com.subflow.payment;

import static com.subflow.TestData.START;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.subflow.domain.Money;
import com.subflow.service.ChargeRequest;
import com.subflow.service.PaymentTimeoutException;
import com.subflow.testing.FakePaymentCharger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class RetryingPaymentChargerTest {
  private final List<Duration> waits = new ArrayList<>();
  private final ChargeRequest request =
      new ChargeRequest(7, Money.usd("203.90"), "subscription-7-period-2026-01-01");

  @Test
  void aTimeoutIsRetriedAndTheCustomerIsChargedOnce() {
    FakePaymentCharger payFast = new FakePaymentCharger(START).timeOutAfterCharging(1);
    RetryingPaymentCharger charger =
        new RetryingPaymentCharger(payFast, RetryPolicy.DEFAULT, waits::add);

    charger.charge(request);

    assertEquals(1, payFast.charges().size());
    assertEquals(List.of(Duration.ofSeconds(1)), waits);
  }

  @Test
  void afterTheLastAttemptTheUnknownOutcomeIsReported() {
    FakePaymentCharger payFast = new FakePaymentCharger(START).timeOutAfterCharging(5);
    RetryingPaymentCharger charger =
        new RetryingPaymentCharger(payFast, RetryPolicy.DEFAULT, waits::add);

    assertThrows(PaymentTimeoutException.class, () -> charger.charge(request));
    assertEquals(List.of(Duration.ofSeconds(1), Duration.ofSeconds(2)), waits);
    assertEquals(1, payFast.charges().size());
  }
}
