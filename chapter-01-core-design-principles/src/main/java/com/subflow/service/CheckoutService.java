package com.subflow.service;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Plan;
import com.subflow.domain.PricingPolicy;
import java.math.BigDecimal;

public class CheckoutService {
  private final PricingPolicy pricing;

  public CheckoutService(PricingPolicy pricing) {
    this.pricing = pricing;
  }

  public BigDecimal priceFor(Plan plan, BillingCycle cycle) {
    return pricing.priceFor(plan, cycle);
  }
}
