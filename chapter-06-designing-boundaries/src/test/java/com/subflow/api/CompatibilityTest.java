package com.subflow.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Money;
import com.subflow.domain.Plan;
import com.subflow.domain.PriceQuote;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Fails if a field of version 1 is renamed, removed or reordered. */
class CompatibilityTest {

  @Test
  void priceQuoteResponseKeepsItsVersion1Fields() {
    List<String> names = Arrays.stream(PriceQuoteResponse.class.getRecordComponents())
        .map(RecordComponent::getName).toList();
    assertEquals(List.of("plan", "billingCycle", "currency", "listPrice", "price"), names);
  }

  @Test
  void amountsAreSentAsExactDecimalText() {
    PriceQuoteResponse r = PriceQuoteResponse.from(new PriceQuote(Plan.PRO,
        BillingCycle.ANNUAL, Money.usd("239.88"), Money.usd("203.90")));
    assertEquals("203.90", r.price());
    assertEquals("USD", r.currency());
  }
}
