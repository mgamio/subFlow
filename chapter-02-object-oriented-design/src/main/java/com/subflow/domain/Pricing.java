package com.subflow.domain;

/** Decides, in one place, which pricing policy applies to a customer. */
public class Pricing {
  private final PricingPolicy standard = new StandardPricing();

  public PricingPolicy forCustomer(Customer customer) {
    return customer.partner()
        .<PricingPolicy>map(PartnerPricing::new)
        .orElse(standard);
  }
}
