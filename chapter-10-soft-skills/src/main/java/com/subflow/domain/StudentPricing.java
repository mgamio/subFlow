package com.subflow.domain;

import java.math.BigDecimal;

/** Students pay 20% less than the list price, monthly or annual. */
public class StudentPricing implements PricingPolicy {
  private static final BigDecimal STUDENT_DISCOUNT =
      new BigDecimal("0.20");

  @Override
  public Money priceFor(Plan plan, BillingCycle cycle) {
    return plan.listPrice(cycle).times(
        BigDecimal.ONE.subtract(STUDENT_DISCOUNT));
  }
}
