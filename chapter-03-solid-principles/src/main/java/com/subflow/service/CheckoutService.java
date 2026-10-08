package com.subflow.service;

import com.subflow.domain.Customer;
import com.subflow.domain.Money;
import com.subflow.domain.Payment;
import com.subflow.domain.Pricing;
import com.subflow.domain.Subscription;

public class CheckoutService {
  private final Pricing pricing;
  private final PaymentCharger payments;
  private final PaymentRepository paymentRecords;

  public CheckoutService(Pricing pricing, PaymentCharger payments,
                         PaymentRepository paymentRecords) {
    this.pricing = pricing;
    this.payments = payments;
    this.paymentRecords = paymentRecords;
  }

  public Money priceFor(Customer customer, Subscription subscription) {
    return pricing.forCustomer(customer)
        .priceFor(subscription.plan(), subscription.cycle());
  }

  public Payment pay(Customer customer, Subscription subscription) {
    Payment payment = payments.charge(subscription.id(),
        priceFor(customer, subscription));
    paymentRecords.save(payment);
    return payment;
  }
}
