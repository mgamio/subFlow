package com.subflow.domain;

import java.math.BigDecimal;

public enum Plan {
  BASIC(Money.usd("9.99")),
  PRO(Money.usd("19.99"));

  private static final BigDecimal MONTHS_PER_YEAR =
      BigDecimal.valueOf(12);

  private final Money monthlyPrice;

  Plan(Money monthlyPrice) {
    this.monthlyPrice = monthlyPrice;
  }

  public Money monthlyPrice() {
    return monthlyPrice;
  }

  /** The price before any discount. */
  public Money listPrice(BillingCycle cycle) {
    return cycle == BillingCycle.MONTHLY
        ? monthlyPrice
        : monthlyPrice.times(MONTHS_PER_YEAR);
  }
}
