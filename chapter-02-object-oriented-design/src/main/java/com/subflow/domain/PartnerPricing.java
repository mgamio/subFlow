package com.subflow.domain;

import java.math.BigDecimal;

/** Partner discounts replace the annual discount; they never combine. */
public class PartnerPricing implements PricingPolicy {
  private final PartnerAgreement agreement;

  public PartnerPricing(PartnerAgreement agreement) {
    this.agreement = agreement;
  }

  @Override
  public Money priceFor(Plan plan, BillingCycle cycle) {
    return plan.listPrice(cycle).times(
        BigDecimal.ONE.subtract(agreement.discount()));
  }
}
