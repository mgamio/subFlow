package com.subflow.service;

import com.subflow.domain.Customer;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.Pricing;
import com.subflow.domain.Subscription;
import java.time.Clock;
import java.time.LocalDate;

public class CheckoutService {
  private final Pricing pricing;
  private final PaymentCharger payments;
  private final PaymentRepository paymentRecords;
  private final Clock clock;

  public CheckoutService(Pricing pricing, PaymentCharger payments,
                         PaymentRepository paymentRecords, Clock clock) {
    this.pricing = pricing;
    this.payments = payments;
    this.paymentRecords = paymentRecords;
    this.clock = clock;
  }

  public Money priceFor(Customer customer, Subscription subscription) {
    return pricing.forCustomer(customer)
        .priceFor(subscription.plan(), subscription.cycle());
  }

  public Payment pay(Customer customer, Subscription subscription) {
    LocalDate today = LocalDate.now(clock);
    ChargeRequest request = ChargeRequest.forBillingPeriod(
        subscription.id(), priceFor(customer, subscription),
        subscription.periodStartOn(today));
    Payment payment = payments.charge(request);
    paymentRecords.save(payment);
    return payment;
  }
}
