package com.subflow.payment.cardhub;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.service.PaymentCharger;
import com.subflow.service.PaymentRefunder;
import com.subflow.service.RefundResult;
import com.subflow.service.RefundStatus;
import java.time.Clock;
import java.time.LocalDate;
import java.util.UUID;

/**
 * CardHub has no refund API: refunds are made by hand in its
 * dashboard. This adapter keeps the PaymentRefunder contract by
 * registering a manual request instead of throwing.
 */
public class CardHubPaymentAdapter
    implements PaymentCharger, PaymentRefunder {

  private final ManualRefundQueue manualRefunds;
  private final Clock clock;

  public CardHubPaymentAdapter(ManualRefundQueue manualRefunds,
                               Clock clock) {
    this.manualRefunds = manualRefunds;
    this.clock = clock;
  }

  @Override
  public Payment charge(long subscriptionId, Money amount) {
    // A real adapter calls CardHub's API here.
    return new Payment("ch_" + UUID.randomUUID(), subscriptionId,
        PaymentProvider.CARDHUB, amount, LocalDate.now(clock));
  }

  @Override
  public RefundResult refund(Payment payment) {
    String ticket = manualRefunds.request(payment);
    return new RefundResult(RefundStatus.PENDING_MANUAL, ticket);
  }
}
