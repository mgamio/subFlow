package com.subflow.service;

import static com.subflow.TestData.START;
import static com.subflow.TestData.annualPro;
import static com.subflow.TestData.clockOn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.subflow.api.ApiResponse;
import com.subflow.api.SubscriptionApi;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.domain.SubscriptionStatus;
import com.subflow.domain.events.DomainEvent;
import com.subflow.domain.events.SubscriptionCanceled;
import com.subflow.testing.DirectTransactions;
import com.subflow.testing.InMemoryOutbox;
import com.subflow.testing.InMemoryPayments;
import com.subflow.testing.InMemoryProcessedEvents;
import com.subflow.testing.InMemorySubscriptions;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CancellationServiceTest {
  private final InMemorySubscriptions subscriptions = new InMemorySubscriptions();
  private final InMemoryOutbox outbox = new InMemoryOutbox();
  private final DirectTransactions transactions = new DirectTransactions();
  private final CancellationService service = new CancellationService(
      subscriptions, outbox, transactions, clockOn(START.plusDays(3)));
  private final Caller owner = new Caller(1);       // annualPro() belongs to customer 1

  @BeforeEach
  void store() {
    subscriptions.save(annualPro());
  }

  @Test
  void theOldServiceLetsAnyoneCancelAnySubscription() {
    new examples.ch06.authorization.before.CancellationService(subscriptions).cancel(7);
    assertEquals(SubscriptionStatus.CANCELED,
        subscriptions.findById(7).orElseThrow().status());   // nobody checked who asked
  }

  @Test
  void anotherCustomerGetsNotFoundAndNothingChanges() {
    ApiResponse<CancellationResult> response =
        new SubscriptionApi(service).cancel(new Caller(99), 7);
    assertEquals(404, response.status());
    assertEquals(SubscriptionStatus.ACTIVE, subscriptions.findById(7).orElseThrow().status());
  }

  @Test
  void theChangeAndTheEventAreSavedInOneTransaction() {
    service.cancel(owner, 7);
    assertEquals(1, transactions.count());
    assertEquals(List.of(new SubscriptionCanceled(7, 1, START.plusDays(3), true)),
        outbox.pending());
  }

  @Test
  void ofTwoConcurrentCancellationsOnlyOneWins() {
    var readByA = subscriptions.findById(7).orElseThrow();
    var readByB = subscriptions.findById(7).orElseThrow();   // same moment, other server

    subscriptions.update(readByA, readByA.cancel());
    assertThrows(ConcurrentUpdateException.class,
        () -> subscriptions.update(readByB, readByB.cancel()));
  }

  @Test
  void aSecondCancellationIsAConflictForTheCaller() {
    service.cancel(owner, 7);
    assertEquals(409, new SubscriptionApi(service).cancel(owner, 7).status());
  }

  @Test
  void theRefundHappensOnceEvenIfTheEventIsDeliveredTwice() {
    InMemoryPayments payments = new InMemoryPayments();
    payments.save(new Payment("pf_1", 7, PaymentProvider.PAYFAST, Money.usd("203.90"), START));
    List<Payment> refunded = new ArrayList<>();
    RefundOnCancellation refunds = new RefundOnCancellation(payments,
        payment -> { refunded.add(payment); return new RefundResult(RefundStatus.REFUNDED, "re_1"); },
        new InMemoryProcessedEvents());

    service.cancel(owner, 7);
    List<DomainEvent> delivered = new ArrayList<>();
    new OutboxRelay(outbox, delivered::add).relayPending();
    delivered.forEach(refunds::on);
    delivered.forEach(refunds::on);                      // at-least-once: a duplicate

    assertEquals(1, refunded.size());
    assertEquals(List.of(), outbox.pending());
  }

  @Test
  void ifPublishingFailsTheEventStaysPending() {
    service.cancel(owner, 7);
    OutboxRelay relay = new OutboxRelay(outbox, event -> {
      throw new IllegalStateException("broker down");
    });
    assertThrows(IllegalStateException.class, relay::relayPending);
    assertEquals(1, outbox.pending().size());
  }
}
