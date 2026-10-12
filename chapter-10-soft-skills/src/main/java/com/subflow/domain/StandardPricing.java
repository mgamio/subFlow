package com.subflow.domain;

import java.math.BigDecimal;

public class StandardPricing implements PricingPolicy {
  private static final BigDecimal ANNUAL_DISCOUNT =
      new BigDecimal("0.15");

  @Override
  public Money priceFor(Plan plan, BillingCycle cycle) {
    Money listPrice = plan.listPrice(cycle);
    if (cycle == BillingCycle.MONTHLY) {
      return listPrice;
    }
    return listPrice.times(
        BigDecimal.ONE.subtract(ANNUAL_DISCOUNT));
  }
}
