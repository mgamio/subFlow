package com.subflow.domain;

public interface PricingPolicy {
  Money priceFor(Plan plan, BillingCycle cycle);
}
