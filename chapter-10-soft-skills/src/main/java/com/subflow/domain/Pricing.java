package com.subflow.domain;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Chooses a customer's pricing policy. A customer pays the lowest price
 * of every offer they qualify for; offers never combine (ADR 0007).
 */
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
    List<PricingPolicy> offers = new ArrayList<>(List.of(standard));
    for (PricingRule rule : rules.get()) {
      rule.policyFor(customer, today).ifPresent(offers::add);
    }
    return new LowestPrice(offers);
  }
}
