package com.subflow.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingPolicy {

  private static final BigDecimal ANNUAL_DISCOUNT =
      new BigDecimal("0.15");
  private static final int MONTHS_PER_YEAR = 12;

  public BigDecimal priceFor(Plan plan, BillingCycle cycle) {
    if (cycle == BillingCycle.MONTHLY) {
      return plan.monthlyPrice();
    }
    BigDecimal fullYear = plan.monthlyPrice()
        .multiply(BigDecimal.valueOf(MONTHS_PER_YEAR));
    return fullYear
        .multiply(BigDecimal.ONE.subtract(ANNUAL_DISCOUNT))
        .setScale(2, RoundingMode.HALF_UP);
  }
}
