package com.subflow.service;

import static com.subflow.TestData.START;
import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.subflow.domain.PartnerRule;
import com.subflow.domain.PriceQuote;
import com.subflow.domain.Pricing;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Test;

class CachedPriceQuotesTest {

  /** A clock a test can move forward. */
  static class TestClock extends Clock {
    private Instant now = START.atStartOfDay(ZoneOffset.UTC).toInstant();
    void advance(Duration d) { now = now.plus(d); }
    @Override public ZoneId getZone() { return ZoneOffset.UTC; }
    @Override public Clock withZone(ZoneId zone) { return this; }
    @Override public Instant instant() { return now; }
  }

  @Test
  void quotesAreReusedUntilTheyExpire() {
    TestClock clock = new TestClock();
    CachedPriceQuotes cache = new CachedPriceQuotes(
        new PriceQuoteService(new Pricing(List.of(new PartnerRule()), clock)),
        clock, Duration.ofMinutes(5));

    List<PriceQuote> first = cache.quotesFor(regularCustomer());
    clock.advance(Duration.ofMinutes(4));
    assertSame(first, cache.quotesFor(regularCustomer()));
    clock.advance(Duration.ofMinutes(1));
    List<PriceQuote> refreshed = cache.quotesFor(regularCustomer());
    assertEquals(first, refreshed);
    assertEquals(false, first == refreshed);
  }
}
