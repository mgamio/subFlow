package com.subflow.app;

import static com.subflow.TestData.START;
import static com.subflow.TestData.annualPro;
import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.payfast.sdk.PayFastClient;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.service.CancellationResult;
import com.subflow.service.RefundStatus;
import com.subflow.testing.InMemoryPayments;
import com.subflow.testing.InMemorySubscriptions;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

/** The whole wiring, with in-memory storage and no real waiting. */
class SubFlowApplicationTest {
  private final InMemorySubscriptions subscriptions = new InMemorySubscriptions();
  private final InMemoryPayments payments = new InMemoryPayments();
  private final PayFastClient payFast = new PayFastClient("test-key", clockOn(START));

  private SubFlowApplication appOn(LocalDate day) {
    return new SubFlowApplication(payFast, payment -> "TICKET-1",
        subscriptions, payments, List.of(), clockOn(day), duration -> { });
  }

  @Test
  void payThenCancelWithinTheWindowRefundsThroughPayFast() {
    subscriptions.save(annualPro());
    Payment payment = appOn(START).checkout().pay(regularCustomer(), annualPro());
    assertEquals(Money.usd("203.90"), payment.amount());

    CancellationResult result = appOn(START.plusDays(3)).cancellation().cancel(7);
    assertEquals(RefundStatus.REFUNDED, result.refund().status());
  }
}
