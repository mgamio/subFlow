package com.subflow.service;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Customer;
import com.subflow.domain.Money;
import com.subflow.domain.Plan;
import com.subflow.domain.Pricing;

public class CheckoutService {
  private final Pricing pricing;

  public CheckoutService(Pricing pricing) {
    this.pricing = pricing;
  }

  public Money priceFor(Customer customer, Plan plan,
                        BillingCycle cycle) {
    return pricing.forCustomer(customer).priceFor(plan, cycle);
  }
}
