package com.subflow.payment.payfast;

import com.payfast.sdk.PayFastCharge;
import com.payfast.sdk.PayFastChargeRequest;
import com.payfast.sdk.PayFastClient;
import com.payfast.sdk.PayFastException;
import com.payfast.sdk.PayFastRefund;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.PaymentProvider;
import com.subflow.service.PaymentCharger;
import com.subflow.service.PaymentDeclinedException;
import com.subflow.service.PaymentRefunder;
import com.subflow.service.RefundResult;
import com.subflow.service.RefundStatus;
import com.subflow.service.TransactionHistory;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

/** Translates between SubFlow's interfaces and PayFast's SDK. */
public class PayFastPaymentAdapter
    implements PaymentCharger, PaymentRefunder, TransactionHistory {

  private static final String REFERENCE_PREFIX = "subscription-";

  private final PayFastClient payFast;
  private final Clock clock;

  public PayFastPaymentAdapter(PayFastClient payFast, Clock clock) {
    this.payFast = payFast;
    this.clock = clock;
  }

  @Override
  public Payment charge(long subscriptionId, Money amount) {
    PayFastCharge charge;
    try {
      charge = payFast.createCharge(new PayFastChargeRequest(
          REFERENCE_PREFIX + subscriptionId,
          toMinorUnits(amount),
          amount.currency().getCurrencyCode()));
    } catch (PayFastException e) {
      throw new PaymentDeclinedException(e.getMessage());
    }
    return new Payment(charge.id(), subscriptionId,
        PaymentProvider.PAYFAST, amount, LocalDate.now(clock));
  }

  @Override
  public RefundResult refund(Payment payment) {
    PayFastRefund refund = payFast.createRefund(
        payment.id(), toMinorUnits(payment.amount()));
    return new RefundResult(RefundStatus.REFUNDED, refund.id());
  }

  @Override
  public List<Payment> paymentsBetween(LocalDate from, LocalDate to) {
    return payFast.listCharges(from, to).stream()
        .map(this::toPayment)
        .toList();
  }

  private Payment toPayment(PayFastCharge charge) {
    long subscriptionId = Long.parseLong(
        charge.customerReference().substring(REFERENCE_PREFIX.length()));
    Currency currency = Currency.getInstance(charge.currency());
    Money amount = new Money(BigDecimal.valueOf(charge.amountInMinorUnits(),
        currency.getDefaultFractionDigits()), currency);
    return new Payment(charge.id(), subscriptionId,
        PaymentProvider.PAYFAST, amount, charge.date());
  }

  private static long toMinorUnits(Money money) {
    return money.amount()
        .movePointRight(money.currency().getDefaultFractionDigits())
        .longValueExact();
  }
}
