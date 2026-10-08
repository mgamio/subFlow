package com.subflow.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import org.junit.jupiter.api.Test;

class PricingPropertiesTest {
  private final Random random = new Random(7);

  @Test
  void noPolicyChargesMoreThanTheListPriceOrNothingAtAll() {
    for (int i = 0; i < 5_000; i++) {
      BigDecimal discount = BigDecimal.valueOf(1 + random.nextInt(98))
          .divide(BigDecimal.valueOf(100), 2, RoundingMode.UNNECESSARY);
      PricingPolicy[] policies = {
          new StandardPricing(),
          new PartnerPricing(new PartnerAgreement("P", discount)) };
      for (PricingPolicy policy : policies) {
        for (Plan plan : Plan.values()) {
          for (BillingCycle cycle : BillingCycle.values()) {
            Money price = policy.priceFor(plan, cycle);
            Money list = plan.listPrice(cycle);
            String example = policy.getClass().getSimpleName() + " "
                + discount + " " + plan + " " + cycle;
            assertTrue(price.amount().signum() > 0, example);
            assertTrue(price.amount().compareTo(list.amount()) <= 0, example);
          }
        }
      }
    }
  }
}
