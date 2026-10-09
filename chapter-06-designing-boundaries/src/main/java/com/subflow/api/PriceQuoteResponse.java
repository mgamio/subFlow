package com.subflow.api;

import com.subflow.domain.PriceQuote;

/**
 * Version 1 of the price contract. Fields may be added; existing fields
 * are never renamed or removed while version 1 is supported.
 */
public record PriceQuoteResponse(String plan, String billingCycle,
                                 String currency, String listPrice,
                                 String price) {

  public static PriceQuoteResponse from(PriceQuote q) {
    return new PriceQuoteResponse(q.plan().name(), q.cycle().name(),
        q.price().currency().getCurrencyCode(),
        q.listPrice().amount().toPlainString(),
        q.price().amount().toPlainString());
  }
}
