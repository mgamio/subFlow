package com.subflow.service;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Customer;
import com.subflow.domain.Plan;
import com.subflow.domain.PriceQuote;
import com.subflow.domain.Pricing;
import com.subflow.domain.PricingPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * The prices a customer sees in any app. The same Pricing that
 * checkout charges with: shown and charged prices cannot differ.
 */
public class PriceQuoteService {
  private final Pricing pricing;

  public PriceQuoteService(Pricing pricing) {
    this.pricing = pricing;
  }

  public List<PriceQuote> quotesFor(Customer customer) {
    PricingPolicy policy = pricing.forCustomer(customer);
    List<PriceQuote> quotes = new ArrayList<>();
    for (Plan plan : Plan.values()) {
      for (BillingCycle cycle : BillingCycle.values()) {
        quotes.add(new PriceQuote(plan, cycle,
            plan.listPrice(cycle), policy.priceFor(plan, cycle)));
      }
    }
    return quotes;
  }
}
