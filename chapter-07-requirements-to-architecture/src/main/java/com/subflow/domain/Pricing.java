package com.subflow.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/** Chooses a customer's pricing policy. Rules are checked in order. */
public class Pricing {
  private final Supplier<List<PricingRule>> rules;
  private final Clock clock;
  private final PricingPolicy standard = new StandardPricing();

  /** Fixed rules, decided when the application starts. */
  public Pricing(List<PricingRule> rules, Clock clock) {
    List<PricingRule> fixed = List.copyOf(rules);
    this.rules = () -> fixed;
    this.clock = clock;
  }

  /** Rules supplied at every call, so they can change while running. */
  public Pricing(Supplier<List<PricingRule>> rules, Clock clock) {
    this.rules = rules;
    this.clock = clock;
  }

  public PricingPolicy forCustomer(Customer customer) {
    LocalDate today = LocalDate.now(clock);
    for (PricingRule rule : rules.get()) {
      Optional<PricingPolicy> policy = rule.policyFor(customer, today);
      if (policy.isPresent()) {
        return policy.get();
      }
    }
    return standard;
  }
}
