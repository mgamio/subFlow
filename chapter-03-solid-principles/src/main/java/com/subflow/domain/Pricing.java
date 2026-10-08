package com.subflow.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Chooses a customer's pricing policy. Rules are checked in order. */
public class Pricing {
  private final List<PricingRule> rules;
  private final Clock clock;
  private final PricingPolicy standard = new StandardPricing();

  public Pricing(List<PricingRule> rules, Clock clock) {
    this.rules = List.copyOf(rules);
    this.clock = clock;
  }

  public PricingPolicy forCustomer(Customer customer) {
    LocalDate today = LocalDate.now(clock);
    for (PricingRule rule : rules) {
      Optional<PricingPolicy> policy =
          rule.policyFor(customer, today);
      if (policy.isPresent()) {
        return policy.get();
      }
    }
    return standard;
  }
}
