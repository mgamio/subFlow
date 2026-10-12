package com.subflow.domain;

import java.time.LocalDate;
import java.util.Optional;

public interface PricingRule {
  Optional<PricingPolicy> policyFor(Customer customer, LocalDate today);
}
