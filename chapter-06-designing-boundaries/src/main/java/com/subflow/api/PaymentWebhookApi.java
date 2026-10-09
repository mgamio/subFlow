package com.subflow.api;

import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.service.PaymentRepository;
import com.subflow.service.ProcessedEvents;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Currency;

public class PaymentWebhookApi {
  private final PaymentRepository payments;
  private final ProcessedEvents processed;
  private final Clock clock;

  public PaymentWebhookApi(PaymentRepository payments,
                           ProcessedEvents processed, Clock clock) {
    this.payments = payments;
    this.processed = processed;
    this.clock = clock;
  }

  // Handles: POST /webhooks/payfast   (signature checked before this call)
  public ApiResponse<Void> receive(PayFastWebhook webhook) {
    if (!"charge.succeeded".equals(webhook.type())) {
      return ApiResponse.ok(null);              // not interested
    }
    if (!processed.markProcessed("payfast-" + webhook.eventId())) {
      return ApiResponse.ok(null);              // already handled: still 200
    }
    Currency currency = Currency.getInstance(webhook.currency());
    Money amount = new Money(BigDecimal.valueOf(webhook.amountInMinorUnits(),
        currency.getDefaultFractionDigits()), currency);
    payments.save(new Payment(webhook.chargeId(), webhook.subscriptionId(),
        PaymentProvider.PAYFAST, amount, LocalDate.now(clock)));
    return ApiResponse.ok(null);
  }
}
