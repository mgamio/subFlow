package com.subflow.service;

import com.subflow.domain.Customer;
import com.subflow.domain.PriceQuote;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Remembers quotes for a short time. For display only: checkout always
 * calculates the price it charges.
 */
public class CachedPriceQuotes {
  private record Entry(List<PriceQuote> quotes, Instant expires) { }

  private final PriceQuoteService quotes;
  private final Clock clock;
  private final Duration timeToLive;
  private final Map<Long, Entry> entries = new ConcurrentHashMap<>();

  public CachedPriceQuotes(PriceQuoteService quotes, Clock clock,
                           Duration timeToLive) {
    this.quotes = quotes;
    this.clock = clock;
    this.timeToLive = timeToLive;
  }

  public List<PriceQuote> quotesFor(Customer customer) {
    Instant now = clock.instant();
    Entry entry = entries.get(customer.id());
    if (entry == null || !now.isBefore(entry.expires())) {
      entry = new Entry(quotes.quotesFor(customer), now.plus(timeToLive));
      entries.put(customer.id(), entry);
    }
    return entry.quotes();
  }

  /** Called when prices change on purpose, such as a campaign start. */
  public void clear() {
    entries.clear();
  }
}
