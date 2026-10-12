package com.subflow.domain;

import java.util.Comparator;
import java.util.List;

/** The customer pays the cheapest of several offers; they never combine. */
public class LowestPrice implements PricingPolicy {
  private final List<PricingPolicy> offers;

  public LowestPrice(List<PricingPolicy> offers) {
    if (offers.isEmpty()) {
      throw new IllegalArgumentException("At least one offer is needed");
    }
    this.offers = List.copyOf(offers);
  }

  @Override
  public Money priceFor(Plan plan, BillingCycle cycle) {
    return offers.stream()
        .map(offer -> offer.priceFor(plan, cycle))
        .min(Comparator.comparing(Money::amount))
        .orElseThrow();
  }
}
