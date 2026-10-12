package com.subflow.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.service.ChargeRequest;
import com.subflow.service.PaymentCharger;
import com.subflow.service.PaymentProviderUnavailableException;
import com.subflow.service.PaymentTimeoutException;
import com.subflow.testing.MovableClock;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import org.junit.jupiter.api.Test;

class FailoverPaymentChargerTest {
  private final MovableClock clock = new MovableClock();
  private final CircuitBreaker breaker = new CircuitBreaker(5, Duration.ofSeconds(30), clock);
  private final List<String> payFastCalls = new ArrayList<>();
  private final List<String> cardHubCharges = new ArrayList<>();

  private final PaymentCharger cardHub = request -> {
    cardHubCharges.add(request.idempotencyKey());
    return new Payment("ch_" + cardHubCharges.size(), request.subscriptionId(),
        PaymentProvider.CARDHUB, request.amount(), LocalDate.of(2027, 1, 4));
  };

  private static ChargeRequest request(int n) {
    return new ChargeRequest(n, Money.usd("19.99"), "subscription-" + n + "-period-2027-01-04");
  }

  private PaymentCharger payFastThatIs(RuntimeException failure) {
    return request -> {
      payFastCalls.add(request.idempotencyKey());
      throw failure;
    };
  }

  @Test
  void whenPayFastIsUnreachableEveryCustomerIsChargedByCardHub() {
    var charger = new FailoverPaymentCharger(payFastThatIs(
        new PaymentProviderUnavailableException("connection refused")), cardHub, breaker);

    for (int n = 1; n <= 20; n++) {
      assertEquals(PaymentProvider.CARDHUB, charger.charge(request(n)).provider());
    }
    assertEquals(5, payFastCalls.size());          // then the breaker stopped trying
    assertEquals(CircuitBreaker.State.OPEN, breaker.state());
  }

  @Test
  void whenPayFastHangsNoChargeIsSentElsewhereUntilTheBreakerOpens() {
    var charger = new FailoverPaymentCharger(payFastThatIs(
        new PaymentTimeoutException("read timed out")), cardHub, breaker);

    for (int n = 1; n <= 5; n++) {
      int k = n;
      assertThrows(PaymentTimeoutException.class, () -> charger.charge(request(k)));
    }
    assertTrue(cardHubCharges.isEmpty());          // unknown outcomes never fail over
    assertEquals(PaymentProvider.CARDHUB, charger.charge(request(6)).provider());
    assertEquals(List.of("subscription-6-period-2027-01-04"), cardHubCharges);
  }

  @Test
  void atPeakCheckoutsRecoverWithinFiveMinutes() {
    // Scenario S2, simulated: 3 checkouts per second; PayFast hangs, and each
    // call to it fails after 33 s (3 attempts of 10 s, plus 1 s and 2 s waits).
    Duration between = Duration.ofMillis(333);
    Duration failsAfter = Duration.ofSeconds(33);
    PriorityQueue<Duration> pendingFailures = new PriorityQueue<>();
    Duration now = Duration.ZERO;
    Duration firstCardHubCharge = null;
    int customersWaiting = 0;

    for (int n = 1; now.compareTo(Duration.ofMinutes(10)) < 0; n++) {
      while (!pendingFailures.isEmpty() && pendingFailures.peek().compareTo(now) <= 0) {
        Duration failedAt = pendingFailures.poll();
        clock.advance(failedAt.minus(elapsedOnClock));
        elapsedOnClock = failedAt;
        breaker.recordFailure();                   // a PayFast call finally failed
      }
      clock.advance(now.minus(elapsedOnClock));
      elapsedOnClock = now;
      if (breaker.allowsRequest()) {
        pendingFailures.add(now.plus(failsAfter));  // this customer waits, then fails
        customersWaiting++;
      } else {
        cardHub.charge(request(n));
        if (firstCardHubCharge == null) firstCardHubCharge = now;
      }
      now = now.plus(between);
    }

    assertTrue(firstCardHubCharge.compareTo(Duration.ofMinutes(5)) < 0,
        "recovered after " + firstCardHubCharge);
    assertEquals(34, firstCardHubCharge.toSeconds());
    // The price: everyone who started a checkout in the first 33 seconds,
    // plus one trial checkout about every minute while the outage lasts.
    assertTrue(customersWaiting > 90 && customersWaiting < 120, "waiting: " + customersWaiting);
  }

  private Duration elapsedOnClock = Duration.ZERO;

  @Test
  void whenPayFastRecoversTrafficReturnsToIt() {
    List<Boolean> payFastUp = new ArrayList<>(List.of(false));
    PaymentCharger payFast = request -> {
      if (!payFastUp.get(0)) throw new PaymentProviderUnavailableException("down");
      return new Payment("pf_1", request.subscriptionId(), PaymentProvider.PAYFAST,
          request.amount(), LocalDate.of(2027, 1, 4));
    };
    var charger = new FailoverPaymentCharger(payFast, cardHub, breaker);
    for (int n = 1; n <= 5; n++) charger.charge(request(n));
    payFastUp.set(0, true);
    clock.advance(Duration.ofSeconds(30));
    assertEquals(PaymentProvider.PAYFAST, charger.charge(request(6)).provider());
    assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
  }
}
