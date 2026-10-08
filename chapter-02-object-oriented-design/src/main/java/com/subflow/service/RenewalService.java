package com.subflow.service;

import com.subflow.domain.Customer;
import com.subflow.domain.Pricing;
import com.subflow.domain.RenewalQuote;
import com.subflow.domain.Subscription;

public class RenewalService {
  private final Pricing pricing;

  public RenewalService(Pricing pricing) {
    this.pricing = pricing;
  }

  public RenewalQuote quoteFor(Subscription subscription,
                               Customer customer) {
    return new RenewalQuote(
        subscription.plan().listPrice(subscription.cycle()),
        pricing.forCustomer(customer)
            .priceFor(subscription.plan(), subscription.cycle()));
  }
}
