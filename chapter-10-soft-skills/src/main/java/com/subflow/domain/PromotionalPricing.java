package com.subflow.domain;

import java.math.BigDecimal;

/** A campaign replaces the annual discount; monthly prices do not change. */
public class PromotionalPricing implements PricingPolicy {
  private final Campaign campaign;

  public PromotionalPricing(Campaign campaign) {
    this.campaign = campaign;
  }

  @Override
  public Money priceFor(Plan plan, BillingCycle cycle) {
    Money listPrice = plan.listPrice(cycle);
    if (cycle == BillingCycle.MONTHLY) {
      return listPrice;
    }
    return listPrice.times(
        BigDecimal.ONE.subtract(campaign.annualDiscount()));
  }
}
