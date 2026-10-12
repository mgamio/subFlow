package com.subflow.domain;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

/** Customers with an email address at a university get student prices. */
public class StudentRule implements PricingRule {
  /** Academic domains in the countries SubFlow sells in. */
  public static final Set<String> ACADEMIC_DOMAINS =
      Set.of("edu", "edu.pe", "edu.co", "edu.mx", "edu.ar", "ac.uk");

  private final Set<String> academicDomains;

  public StudentRule(Set<String> academicDomains) {
    this.academicDomains = Set.copyOf(academicDomains);
  }

  @Override
  public Optional<PricingPolicy> policyFor(Customer customer,
                                           LocalDate today) {
    String domain =
        customer.email().substring(customer.email().indexOf('@') + 1);
    boolean academic = academicDomains.stream()
        .anyMatch(d -> domain.endsWith("." + d));
    return academic ? Optional.of(new StudentPricing()) : Optional.empty();
  }
}
