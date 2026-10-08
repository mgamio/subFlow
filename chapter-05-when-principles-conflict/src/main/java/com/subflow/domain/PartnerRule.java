package com.subflow.domain;

import java.time.LocalDate;
import java.util.Optional;

public class PartnerRule implements PricingRule {

  @Override
  public Optional<PricingPolicy> policyFor(Customer customer,
                                           LocalDate today) {
    return customer.partner().map(PartnerPricing::new);
  }
}
