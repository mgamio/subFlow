package com.subflow.service;

import static com.subflow.TestData.START;
import static com.subflow.TestData.annualPro;
import static com.subflow.TestData.clockOn;
import static com.subflow.TestData.partnerCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.PartnerRule;
import com.subflow.domain.Plan;
import com.subflow.domain.PriceQuote;
import com.subflow.domain.Pricing;
import com.subflow.testing.FakePaymentCharger;
import com.subflow.testing.InMemoryPayments;
import java.util.List;
import org.junit.jupiter.api.Test;

class PriceQuoteServiceTest {
  private final Pricing pricing = new Pricing(List.of(new PartnerRule()), clockOn(START));

  @Test
  void theQuotedPriceIsThePriceCharged() {
    PriceQuote quote = new PriceQuoteService(pricing).quotesFor(partnerCustomer()).stream()
        .filter(q -> q.plan() == Plan.PRO && q.cycle() == BillingCycle.ANNUAL)
        .findFirst().orElseThrow();

    FakePaymentCharger payFast = new FakePaymentCharger(START);
    new CheckoutService(pricing, payFast, new InMemoryPayments(), clockOn(START))
        .pay(partnerCustomer(), annualPro());

    assertEquals(quote.price(), payFast.charges().get(0).amount());
  }

  @Test
  void everyPlanAndCycleIsQuoted() {
    assertEquals(4, new PriceQuoteService(pricing).quotesFor(partnerCustomer()).size());
  }
}
