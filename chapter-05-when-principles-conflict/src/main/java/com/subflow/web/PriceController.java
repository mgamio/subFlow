package com.subflow.web;

import com.subflow.domain.PriceQuote;
import com.subflow.service.CustomerDirectory;
import com.subflow.service.PriceQuoteService;
import java.util.List;

public class PriceController {
  private final PriceQuoteService quotes;
  private final CustomerDirectory customers;

  public PriceController(PriceQuoteService quotes,
                         CustomerDirectory customers) {
    this.quotes = quotes;
    this.customers = customers;
  }

  // Handles: GET /customers/{id}/prices  (used by the web and mobile apps)
  public List<PriceQuote> prices(long customerId) {
    return customers.findById(customerId)
        .map(quotes::quotesFor)
        .orElseThrow(() -> new IllegalArgumentException(
            "Unknown customer " + customerId));
  }
}
