package com.subflow.domain;

import java.time.LocalDate;
import java.util.Optional;

public class CampaignRule implements PricingRule {
  private final Campaign campaign;

  public CampaignRule(Campaign campaign) {
    this.campaign = campaign;
  }

  @Override
  public Optional<PricingPolicy> policyFor(Customer customer,
                                           LocalDate today) {
    if (!campaign.isRunningOn(today)) {
      return Optional.empty();
    }
    return Optional.of(new PromotionalPricing(campaign));
  }
}
