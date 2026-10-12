package com.subflow.domain;

import java.time.temporal.ChronoUnit;

public enum BillingCycle {
  MONTHLY(ChronoUnit.MONTHS),
  ANNUAL(ChronoUnit.YEARS);

  private final ChronoUnit unit;

  BillingCycle(ChronoUnit unit) {
    this.unit = unit;
  }

  public ChronoUnit unit() {
    return unit;
  }
}
