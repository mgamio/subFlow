package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PricingPolicyTest {
  private final PricingPolicy pricing = new PricingPolicy();

  @Test
  void annualPlanGetsTheDiscount() {
    assertEquals(new BigDecimal("203.90"),
        pricing.priceFor(Plan.PRO, BillingCycle.ANNUAL));
  }

  @Test
  void monthlyPlanPaysTheListPrice() {
    assertEquals(new BigDecimal("19.99"),
        pricing.priceFor(Plan.PRO, BillingCycle.MONTHLY));
  }
}
