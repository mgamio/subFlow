package com.subflow.domain;

import java.math.BigDecimal;

public enum Plan {
  BASIC(new BigDecimal("9.99")),
  PRO(new BigDecimal("19.99"));

  private final BigDecimal monthlyPrice;

  Plan(BigDecimal monthlyPrice) {
    this.monthlyPrice = monthlyPrice;
  }

  public BigDecimal monthlyPrice() {
    return monthlyPrice;
  }
}
