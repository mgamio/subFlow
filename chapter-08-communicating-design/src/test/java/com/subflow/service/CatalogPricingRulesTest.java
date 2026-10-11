package com.subflow.service;

import static com.subflow.TestData.regularCustomer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subflow.domain.BillingCycle;
import com.subflow.domain.Campaign;
import com.subflow.domain.Money;
import com.subflow.domain.Plan;
import com.subflow.domain.Pricing;
import com.subflow.testing.MovableClock;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class CatalogPricingRulesTest {

  @Test
  void aNewCampaignIsLiveWithinSixtySecondsWithoutANewPricing() {
    MovableClock clock = new MovableClock();      // 2027-01-04
    List<Campaign> table = new ArrayList<>();
    Pricing pricing = new Pricing(
        new CatalogPricingRules(() -> List.copyOf(table), clock, Duration.ofSeconds(60)),
        clock);

    assertEquals(Money.usd("203.90"),
        pricing.forCustomer(regularCustomer()).priceFor(Plan.PRO, BillingCycle.ANNUAL));

    table.add(new Campaign("New Year", new BigDecimal("0.30"),
        LocalDate.of(2027, 1, 1), LocalDate.of(2027, 1, 31)));   // marketing saves it
    clock.advance(Duration.ofSeconds(59));
    assertEquals(Money.usd("203.90"),
        pricing.forCustomer(regularCustomer()).priceFor(Plan.PRO, BillingCycle.ANNUAL));

    clock.advance(Duration.ofSeconds(1));
    assertEquals(Money.usd("167.92"),
        pricing.forCustomer(regularCustomer()).priceFor(Plan.PRO, BillingCycle.ANNUAL));
  }
}
