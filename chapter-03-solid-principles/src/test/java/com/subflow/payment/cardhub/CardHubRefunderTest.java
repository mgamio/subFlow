package com.subflow.payment.cardhub;

import static com.subflow.TestData.START;
import static com.subflow.TestData.clockOn;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.service.PaymentRefunder;
import com.subflow.service.PaymentRefunderContract;
import com.subflow.service.RefundResult;
import com.subflow.service.RefundStatus;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CardHubRefunderTest extends PaymentRefunderContract {
  private final List<Payment> queued = new ArrayList<>();
  private final CardHubPaymentAdapter adapter = new CardHubPaymentAdapter(
      payment -> { queued.add(payment); return "TICKET-" + queued.size(); },
      clockOn(START));

  @Override
  protected PaymentRefunder refunder() {
    return adapter;
  }

  @Override
  protected Payment validPayment() {
    return adapter.charge(7, Money.usd("19.99"));
  }

  @Test
  void cardHubRefundsBecomeManualRequests() {
    Payment payment = validPayment();
    RefundResult result = adapter.refund(payment);

    assertEquals(RefundStatus.PENDING_MANUAL, result.status());
    assertEquals("TICKET-1", result.reference());
    assertEquals(List.of(payment), queued);
  }
}
