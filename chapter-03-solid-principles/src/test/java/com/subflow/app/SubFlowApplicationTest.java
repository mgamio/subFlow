package com.subflow.app;

import static com.subflow.TestData.START;
import static com.subflow.TestData.annualPro;
import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.payfast.sdk.PayFastClient;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.domain.Subscription;
import com.subflow.service.CancellationResult;
import com.subflow.service.PaymentRepository;
import com.subflow.service.RefundStatus;
import com.subflow.service.SubscriptionRepository;
import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** The whole wiring, end to end, with in-memory storage. */
class SubFlowApplicationTest {
  private final Map<Long, Subscription> subscriptionRows = new HashMap<>();
  private final Map<Long, Payment> paymentRows = new HashMap<>();

  private final SubscriptionRepository subscriptions = new SubscriptionRepository() {
    public Optional<Subscription> findById(long id) {
      return Optional.ofNullable(subscriptionRows.get(id));
    }
    public void save(Subscription s) { subscriptionRows.put(s.id(), s); }
  };

  private final PaymentRepository payments = new PaymentRepository() {
    public void save(Payment p) { paymentRows.put(p.subscriptionId(), p); }
    public Optional<Payment> lastPaymentFor(long subscriptionId) {
      return Optional.ofNullable(paymentRows.get(subscriptionId));
    }
  };

  /** One provider for the whole test: it remembers the charges it took. */
  private final PayFastClient payFast = new PayFastClient("test-key", clockOn(START));

  private SubFlowApplication appOn(LocalDate day) {
    Clock clock = clockOn(day);
    return new SubFlowApplication(payFast,
        payment -> "TICKET-1", subscriptions, payments, List.of(), clock);
  }

  @Test
  void payThenCancelWithinTheWindowRefundsThroughPayFast() {
    subscriptionRows.put(7L, annualPro());
    Payment payment = appOn(START).checkout().pay(regularCustomer(), annualPro());

    assertEquals(Money.usd("203.90"), payment.amount());
    assertEquals(PaymentProvider.PAYFAST, payment.provider());

    CancellationResult result = appOn(START.plusDays(3)).cancellation().cancel(7);
    assertEquals(RefundStatus.REFUNDED, result.refund().status());
  }

  @Test
  void cardHubPaymentsAreRefundedManually() {
    subscriptionRows.put(7L, annualPro());
    paymentRows.put(7L, new Payment("ch_9", 7, PaymentProvider.CARDHUB,
        Money.usd("203.90"), START));

    CancellationResult result = appOn(START.plusDays(3)).cancellation().cancel(7);
    assertEquals(RefundStatus.PENDING_MANUAL, result.refund().status());
  }

  @Test
  void noRefundAfterTheWindow() {
    subscriptionRows.put(7L, annualPro());
    CancellationResult result = appOn(START.plusDays(15)).cancellation().cancel(7);
    assertFalse(result.refundDue());
    assertNull(result.refund());
  }
}
