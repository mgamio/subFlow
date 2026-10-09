package com.subflow.api;

import static com.subflow.TestData.START;
import static com.subflow.TestData.clockOn;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.Invoice;
import com.subflow.domain.Money;
import com.subflow.testing.InMemoryPayments;
import com.subflow.testing.InMemoryProcessedEvents;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PaymentWebhookApiTest {
  private final PayFastWebhook webhook = new PayFastWebhook(
      "evt_123", "charge.succeeded", "pf_9", 7, 20390, "USD");

  @Test
  void theOldHandlerIssuesTwoInvoicesForOnePayment() {
    List<Invoice> invoices = new ArrayList<>();
    var old = new examples.ch06.idempotency.before.PaymentWebhookApi(invoices::add);
    old.receive(webhook);
    old.receive(webhook);     // PayFast did not get our 200 in time, and resent
    assertEquals(2, invoices.size());
  }

  @Test
  void aDuplicateNotificationIsAcknowledgedAndIgnored() {
    InMemoryPayments payments = new InMemoryPayments();
    PaymentWebhookApi api = new PaymentWebhookApi(payments,
        new InMemoryProcessedEvents(), clockOn(START));

    assertEquals(200, api.receive(webhook).status());
    assertEquals(200, api.receive(webhook).status());

    assertEquals(1, payments.all().size());
    assertEquals(Money.usd("203.90"), payments.all().get(0).amount());
  }
}
